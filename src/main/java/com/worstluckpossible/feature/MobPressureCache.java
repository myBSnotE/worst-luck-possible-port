package com.worstluckpossible.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;

/** One-second per-player density snapshot shared by all expensive hostile policies. */
public final class MobPressureCache {
	public static final double NEAR_DISTANCE_SQUARED = 32.0D * 32.0D;
	public static final double HARD_DISTANCE_SQUARED = 128.0D * 128.0D;
	private static final int CACHE_TICKS = 20;
	private static final Map<ServerWorld, Map<UUID, Snapshot>> CACHE = new WeakHashMap<>();

	private MobPressureCache() {}

	public static Snapshot get(ServerWorld world, PlayerEntity player) {
		Map<UUID, Snapshot> worldCache = CACHE.computeIfAbsent(world, ignored -> new HashMap<>());
		long now = world.getTime();
		Snapshot cached = worldCache.get(player.getUuid());
		if (cached != null && now - cached.sampleTick() < CACHE_TICKS
				&& (cached.farthestReplaceable() == null || cached.farthestReplaceable().isAlive())) {
			return cached;
		}

		int totalMobs = 0;
		int totalHostiles = 0;
		int nearHostiles = 0;
		MobEntity farthest = null;
		double farthestDistance = NEAR_DISTANCE_SQUARED;

		for (MobEntity mob : world.getEntitiesByClass(
				MobEntity.class,
				player.getBoundingBox().expand(128.0D),
				mob -> mob.isAlive() && player.squaredDistanceTo(mob) < HARD_DISTANCE_SQUARED)) {
			totalMobs++;
			if (mob.getType().getSpawnGroup() != SpawnGroup.MONSTER) {
				continue;
			}
			PlayerEntity nearest = world.getClosestPlayer(mob, -1.0D);
			if (nearest == null || !nearest.getUuid().equals(player.getUuid())) {
				continue;
			}
			totalHostiles++;
			double distance = nearest.squaredDistanceTo(mob);
			if (distance <= NEAR_DISTANCE_SQUARED) {
				nearHostiles++;
			} else if (distance > farthestDistance
					&& !(mob instanceof RaiderEntity)
					&& !mob.isPersistent()
					&& !mob.cannotDespawn()) {
				farthestDistance = distance;
				farthest = mob;
			}
		}

		Snapshot snapshot = new Snapshot(now, totalMobs, totalHostiles, nearHostiles, farthest);
		worldCache.put(player.getUuid(), snapshot);
		return snapshot;
	}

	public static void invalidate(ServerWorld world, PlayerEntity player) {
		Map<UUID, Snapshot> worldCache = CACHE.get(world);
		if (worldCache != null) {
			worldCache.remove(player.getUuid());
		}
	}

	public record Snapshot(long sampleTick, int totalMobs, int totalHostiles,
			int nearHostiles, MobEntity farthestReplaceable) {
		public boolean protectsDistantReservoir(boolean previouslyProtected) {
			if (totalHostiles == 0 || totalHostiles > 140) return false;
			double ratio = (double) nearHostiles / (double) totalHostiles;
			return previouslyProtected ? ratio <= 0.55D : ratio < 0.45D;
		}

		public int replacementBudget() {
			if (totalMobs >= 140) return 0;
			if (totalMobs >= 110) return 1;
			if (totalMobs >= 80) return 2;
			return 4;
		}
	}
}
