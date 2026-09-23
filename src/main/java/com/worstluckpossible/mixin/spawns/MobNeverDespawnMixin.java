package com.worstluckpossible.mixin.spawns;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Protects close hostiles and keeps a temporary reservoir of distant hostiles
 * while too few of them have reached their nearest player. Counts are assigned
 * to each mob's nearest player, cached for one second, and use hysteresis to
 * avoid rapidly toggling the policy. Vanilla's 128-block hard despawn remains.
 */
@Mixin(MobEntity.class)
public class MobNeverDespawnMixin {
	@Unique
	private static final double WORSTLUCK_NEAR_DISTANCE_SQUARED = 32.0D * 32.0D;

	@Unique
	private static final double WORSTLUCK_HARD_DISTANCE_SQUARED = 128.0D * 128.0D;

	@Unique
	private static final int WORSTLUCK_DENSITY_CACHE_TICKS = 20;

	@Unique
	private static final int WORSTLUCK_EMERGENCY_HOSTILE_LIMIT_PER_PLAYER = 140;

	@Unique
	private static final double WORSTLUCK_ENABLE_PROTECTION_RATIO = 0.45D;

	@Unique
	private static final double WORSTLUCK_DISABLE_PROTECTION_RATIO = 0.55D;

	@Unique
	private static final Map<ServerWorld, Map<UUID, WorstluckDensityState>> WORSTLUCK_DENSITY_CACHE =
			new WeakHashMap<>();

	@Inject(method = "checkDespawn", at = @At("HEAD"), cancellable = true)
	private void worstluck$despawnByDistance(CallbackInfo ci) {
		MobEntity self = (MobEntity) (Object) this;
		if (self.getType().getSpawnGroup() != SpawnGroup.MONSTER
				|| self.isPersistent()
				|| self.cannotDespawn()
				|| self.getEntityWorld().getDifficulty() == Difficulty.PEACEFUL) {
			return;
		}

		if (!(self.getEntityWorld() instanceof ServerWorld world)) {
			return;
		}

		PlayerEntity closest = world.getClosestPlayer(self, -1.0D);
		if (closest == null) {
			return;
		}

		double squaredDistance = closest.squaredDistanceTo(self);
		if (squaredDistance <= WORSTLUCK_NEAR_DISTANCE_SQUARED) {
			ci.cancel();
			return;
		}

		if (squaredDistance >= WORSTLUCK_HARD_DISTANCE_SQUARED) {
			self.remove(Entity.RemovalReason.DISCARDED);
			ci.cancel();
			return;
		}

		if (worstluck$shouldProtectDistantReservoir(world, closest)) {
			ci.cancel();
		}
	}

	@Unique
	private static boolean worstluck$shouldProtectDistantReservoir(ServerWorld world, PlayerEntity player) {
		Map<UUID, WorstluckDensityState> worldCache = WORSTLUCK_DENSITY_CACHE.computeIfAbsent(
				world,
				ignored -> new HashMap<>()
		);
		long now = world.getTime();
		UUID playerId = player.getUuid();
		WorstluckDensityState previous = worldCache.get(playerId);
		if (previous != null && now - previous.sampleTick() < WORSTLUCK_DENSITY_CACHE_TICKS) {
			return previous.protectDistant();
		}

		int near = 0;
		int total = 0;
		for (MobEntity candidate : world.getEntitiesByClass(
				MobEntity.class,
				player.getBoundingBox().expand(128.0D),
				candidate -> candidate.getType().getSpawnGroup() == SpawnGroup.MONSTER
						&& !candidate.isPersistent()
						&& !candidate.cannotDespawn())) {
			PlayerEntity nearestPlayer = world.getClosestPlayer(candidate, -1.0D);
			if (nearestPlayer == null || !nearestPlayer.getUuid().equals(playerId)) {
				continue;
			}

			double distanceSquared = nearestPlayer.squaredDistanceTo(candidate);
			if (distanceSquared >= WORSTLUCK_HARD_DISTANCE_SQUARED) {
				continue;
			}

			total++;
			if (distanceSquared <= WORSTLUCK_NEAR_DISTANCE_SQUARED) {
				near++;
			}
		}

		boolean protectDistant = previous != null && previous.protectDistant();
		if (total == 0 || total > WORSTLUCK_EMERGENCY_HOSTILE_LIMIT_PER_PLAYER) {
			protectDistant = false;
		} else {
			double nearRatio = (double) near / (double) total;
			if (protectDistant) {
				if (nearRatio > WORSTLUCK_DISABLE_PROTECTION_RATIO) {
					protectDistant = false;
				}
			} else if (nearRatio < WORSTLUCK_ENABLE_PROTECTION_RATIO) {
				protectDistant = true;
			}
		}

		worldCache.put(playerId, new WorstluckDensityState(now, protectDistant));
		return protectDistant;
	}

	@Unique
	private record WorstluckDensityState(long sampleTick, boolean protectDistant) {
	}
}
