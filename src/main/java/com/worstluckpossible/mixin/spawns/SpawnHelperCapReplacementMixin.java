package com.worstluckpossible.mixin.spawns;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * When the hostile cap is full, permits a few close spawn attempts and swaps
 * each successful close spawn for the farthest non-persistent hostile. Nothing
 * is removed until a valid replacement has actually spawned.
 */
@Mixin(SpawnHelper.Info.class)
public class SpawnHelperCapReplacementMixin {
	@Unique
	private static final int WORSTLUCK_MAX_REPLACEMENTS_PER_TICK = 4;

	@Unique
	private boolean worstluck$replacementMode;

	@Unique
	private int worstluck$replacementCount;

	@Inject(method = "isBelowCap", at = @At("RETURN"), cancellable = true)
	private void worstluck$allowHostileReplacement(SpawnGroup group, CallbackInfoReturnable<Boolean> cir) {
		if (group != SpawnGroup.MONSTER) {
			return;
		}

		worstluck$replacementMode = !Boolean.TRUE.equals(cir.getReturnValue());
		if (worstluck$replacementMode) {
			cir.setReturnValue(true);
		}
	}

	@Inject(method = "canSpawn", at = @At("HEAD"), cancellable = true)
	private void worstluck$limitReplacementAttempts(SpawnGroup group, ChunkPos chunkPos,
			CallbackInfoReturnable<Boolean> cir) {
		if (group == SpawnGroup.MONSTER
				&& worstluck$replacementMode
				&& worstluck$replacementCount >= WORSTLUCK_MAX_REPLACEMENTS_PER_TICK) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "run", at = @At("RETURN"))
	private void worstluck$replaceFarthestHostile(MobEntity spawned, Chunk chunk, CallbackInfo ci) {
		if (!worstluck$replacementMode || spawned.getType().getSpawnGroup() != SpawnGroup.MONSTER) {
			return;
		}

		if (!(spawned.getEntityWorld() instanceof ServerWorld world)) {
			return;
		}

		PlayerEntity spawningPlayer = world.getClosestPlayer(spawned, -1.0D);
		if (spawningPlayer == null) {
			spawned.remove(Entity.RemovalReason.DISCARDED);
			worstluck$replacementCount = WORSTLUCK_MAX_REPLACEMENTS_PER_TICK;
			return;
		}

		MobEntity farthest = null;
		double farthestDistance = 1024.0D;
		for (MobEntity candidate : world.getEntitiesByClass(
				MobEntity.class,
				spawningPlayer.getBoundingBox().expand(128.0D),
				candidate -> candidate != spawned
						&& candidate.getType().getSpawnGroup() == SpawnGroup.MONSTER
						&& !candidate.isPersistent()
						&& !candidate.cannotDespawn())) {
			PlayerEntity nearestPlayer = world.getClosestPlayer(candidate, -1.0D);
			if (nearestPlayer == null) {
				continue;
			}

			double distance = nearestPlayer.squaredDistanceTo(candidate);
			if (distance > farthestDistance && distance < 16384.0D) {
				farthestDistance = distance;
				farthest = candidate;
			}
		}

		if (farthest == null) {
			// The cap is full, but every existing hostile is already close to a
			// player. Discard this over-cap spawn and stop replacement attempts.
			spawned.remove(Entity.RemovalReason.DISCARDED);
			worstluck$replacementCount = WORSTLUCK_MAX_REPLACEMENTS_PER_TICK;
			return;
		}

		farthest.remove(Entity.RemovalReason.DISCARDED);
		worstluck$replacementCount++;
	}
}
