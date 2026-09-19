package com.worstluckpossible.mixin.spawns;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Mobs may spawn right next to the player: the minimum spawn distance check always passes. */
@Mixin(SpawnHelper.class)
public class SpawnHelperProximityMixin {
	@Inject(method = "isAcceptableSpawnPosition", at = @At("HEAD"), cancellable = true, require = 0)
	private static void worstluck$alwaysAcceptable(
			ServerWorld world,
			Chunk chunk,
			BlockPos.Mutable pos,
			double squaredDistance,
			CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}
}
