package com.worstluckpossible.mixin.raid;

import com.worstluckpossible.feature.MobPressureCache;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raid.class)
public class RaidWorstCompositionMixin {
	@Unique private static final int WORSTLUCK_LOW_RAIDER_THRESHOLD = 2;
	@Unique private static final int WORSTLUCK_REFILL_ATTEMPTS = 64;
	@Unique private static final int WORSTLUCK_EMPTY_FAILURE_BATCHES = 5;
	@Unique private static final int WORSTLUCK_REFILL_RADIUS = 32;
	@Unique private static final int WORSTLUCK_MAX_LIGHT = 7;
	@Unique private int worstluck$refillWave = -1;
	@Unique private int worstluck$failedEmptyBatches;
	@Unique private boolean worstluck$allowWaveFinish;
	@Unique private long worstluck$nextRefillTick;

	@Redirect(method = "getBonusCount", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
	private int worstluck$maximumBonusCount(Random random, int bound) {
		return Math.max(0, bound - 1);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void worstluck$refillLowRaidWave(ServerWorld world, CallbackInfo ci) {
		Raid raid = (Raid) (Object) this;
		int wave = raid.getGroupsSpawned();
		if (wave <= 0 || raid.isFinished() || !raid.isActive()) return;
		if (worstluck$refillWave != wave) {
			worstluck$resetRefillState(wave, 0L);
		}

		int raiderCount = raid.getRaiderCount();
		if (worstluck$allowWaveFinish
				|| raiderCount > WORSTLUCK_LOW_RAIDER_THRESHOLD
				|| world.getTime() < worstluck$nextRefillTick) {
			return;
		}

		PlayerEntity player = world.getClosestPlayer(
				raid.getCenter().getX() + 0.5D, raid.getCenter().getY() + 0.5D,
				raid.getCenter().getZ() + 0.5D, 96.0D, false);
		if (player == null || !player.isAlive() || player.isSpectator()) {
			// Do not permanently exhaust the wave merely because the player temporarily left.
			worstluck$nextRefillTick = world.getTime() + 20L;
			return;
		}

		BlockPos spawnPos = worstluck$findDarkVillageSpawn(world, raid, player);
		if (spawnPos == null) {
			worstluck$handleFailedRefill(world, raiderCount);
			return;
		}

		RaiderEntity witch = EntityType.WITCH.create(world, SpawnReason.EVENT);
		if (witch == null) {
			worstluck$handleFailedRefill(world, raiderCount);
			return;
		}
		raid.addRaider(world, wave, witch, spawnPos, false);
		if (world.getEntity(witch.getUuid()) != null && witch.isAlive()) {
			// Success must reopen future refill attempts after this witch is killed.
			worstluck$failedEmptyBatches = 0;
			worstluck$allowWaveFinish = false;
			worstluck$nextRefillTick = world.getTime() + 20L;
		} else {
			raid.removeFromWave(world, witch, true);
			witch.remove(Entity.RemovalReason.DISCARDED);
			worstluck$handleFailedRefill(world, raiderCount);
		}
	}

	@Inject(method = "spawnNextWave", at = @At("RETURN"))
	private void worstluck$replaceDistantHostileWithWitch(ServerWorld world, BlockPos spawnPos, CallbackInfo ci) {
		Raid raid = (Raid) (Object) this;
		worstluck$resetRefillState(raid.getGroupsSpawned(), world.getTime() + 20L);

		PlayerEntity player = world.getClosestPlayer(
				raid.getCenter().getX() + 0.5D, raid.getCenter().getY() + 0.5D,
				raid.getCenter().getZ() + 0.5D, 128.0D, false);
		if (player == null) return;
		MobEntity replacement = MobPressureCache.get(world, player).farthestReplaceable();
		if (replacement == null || !replacement.isAlive()) return;

		RaiderEntity witch = EntityType.WITCH.create(world, SpawnReason.EVENT);
		if (witch == null) return;
		raid.addRaider(world, raid.getGroupsSpawned(), witch, spawnPos, false);
		if (world.getEntity(witch.getUuid()) != null && witch.isAlive()) {
			replacement.remove(Entity.RemovalReason.DISCARDED);
			MobPressureCache.invalidate(world, player);
		}
	}

	@Unique
	private void worstluck$handleFailedRefill(ServerWorld world, int raiderCount) {
		if (raiderCount > 0) {
			// A failed random batch must not disable refills for the rest of the wave.
			worstluck$nextRefillTick = world.getTime() + 20L;
			return;
		}

		// With no raiders left, try several independent 64-position batches before
		// allowing vanilla's end-of-wave countdown to complete.
		worstluck$failedEmptyBatches++;
		if (worstluck$failedEmptyBatches >= WORSTLUCK_EMPTY_FAILURE_BATCHES) {
			worstluck$allowWaveFinish = true;
		} else {
			worstluck$nextRefillTick = world.getTime() + 1L;
		}
	}

	@Unique
	private void worstluck$resetRefillState(int wave, long nextTick) {
		worstluck$refillWave = wave;
		worstluck$failedEmptyBatches = 0;
		worstluck$allowWaveFinish = false;
		worstluck$nextRefillTick = nextTick;
	}

	@Unique
	private static BlockPos worstluck$findDarkVillageSpawn(ServerWorld world, Raid raid, PlayerEntity player) {
		Random random = world.getRandom();
		BlockPos origin = player.getBlockPos();
		for (int attempt = 0; attempt < WORSTLUCK_REFILL_ATTEMPTS; attempt++) {
			int x = origin.getX() + random.nextInt(WORSTLUCK_REFILL_RADIUS * 2 + 1) - WORSTLUCK_REFILL_RADIUS;
			int z = origin.getZ() + random.nextInt(WORSTLUCK_REFILL_RADIUS * 2 + 1) - WORSTLUCK_REFILL_RADIUS;
			int startY = origin.getY() + 16 - random.nextInt(17);
			for (int down = 0; down <= 32; down++) {
				BlockPos pos = new BlockPos(x, startY - down, z);
				BlockPos floor = pos.down();
				if (!world.getWorldBorder().contains(pos)
						|| raid.getCenter().getSquaredDistance(pos) > 96.0D * 96.0D
						|| !world.isRegionLoaded(x - 1, z - 1, x + 1, z + 1)
						|| !world.shouldTickEntityAt(pos)
						|| !world.isNearOccupiedPointOfInterest(pos)
						|| world.getLightLevel(pos) > WORSTLUCK_MAX_LIGHT
						|| !world.getBlockState(floor).isSolidBlock(world, floor)
						|| !world.getBlockState(pos).isAir()
						|| !world.getBlockState(pos.up()).isAir()) {
					continue;
				}
				if (!SpawnRestriction.isSpawnPosAllowed(EntityType.WITCH, world, pos)
						|| !SpawnRestriction.canSpawn(EntityType.WITCH, world, SpawnReason.EVENT, pos, random)) {
					continue;
				}
				RaiderEntity probe = EntityType.WITCH.create(world, SpawnReason.EVENT);
				if (probe == null) return null;
				probe.setPosition(x + 0.5D, pos.getY() + 1.0D, z + 0.5D);
				if (world.doesNotIntersectEntities(probe)
						&& world.isSpaceEmpty(probe)
						&& !world.containsFluid(probe.getBoundingBox())) {
					return pos;
				}
			}
		}
		return null;
	}
}
