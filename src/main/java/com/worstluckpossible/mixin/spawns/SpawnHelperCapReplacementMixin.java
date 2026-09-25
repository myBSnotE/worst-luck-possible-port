package com.worstluckpossible.mixin.spawns;

import com.worstluckpossible.feature.MobPressureCache;
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

@Mixin(SpawnHelper.Info.class)
public class SpawnHelperCapReplacementMixin {
	@Unique private boolean worstluck$replacementMode;
	@Unique private int worstluck$replacementCount;

	@Inject(method = "isBelowCap", at = @At("RETURN"), cancellable = true)
	private void worstluck$allowHostileReplacement(SpawnGroup group, CallbackInfoReturnable<Boolean> cir) {
		if (group != SpawnGroup.MONSTER) return;
		worstluck$replacementMode = !Boolean.TRUE.equals(cir.getReturnValue());
		if (worstluck$replacementMode) cir.setReturnValue(true);
	}

	@Inject(method = "canSpawn", at = @At("HEAD"), cancellable = true)
	private void worstluck$limitReplacementAttempts(SpawnGroup group, ChunkPos chunkPos,
			CallbackInfoReturnable<Boolean> cir) {
		if (group == SpawnGroup.MONSTER && worstluck$replacementMode && worstluck$replacementCount >= 4) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "run", at = @At("RETURN"))
	private void worstluck$replaceFarthestHostile(MobEntity spawned, Chunk chunk, CallbackInfo ci) {
		if (!worstluck$replacementMode || spawned.getType().getSpawnGroup() != SpawnGroup.MONSTER
				|| !(spawned.getEntityWorld() instanceof ServerWorld world)) return;
		PlayerEntity player = world.getClosestPlayer(spawned, -1.0D);
		if (player == null) {
			spawned.remove(Entity.RemovalReason.DISCARDED);
			worstluck$replacementCount = 4;
			return;
		}

		MobPressureCache.Snapshot snapshot = MobPressureCache.get(world, player);
		int budget = snapshot.replacementBudget();
		MobEntity farthest = snapshot.farthestReplaceable();
		if (worstluck$replacementCount >= budget || farthest == null || !farthest.isAlive()) {
			spawned.remove(Entity.RemovalReason.DISCARDED);
			worstluck$replacementCount = 4;
			return;
		}
		farthest.remove(Entity.RemovalReason.DISCARDED);
		MobPressureCache.invalidate(world, player);
		worstluck$replacementCount++;
	}
}
