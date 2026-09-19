package com.worstluckpossible.mixin.spawns;

import net.minecraft.util.math.random.Random;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Maximizes only the pack-size roll without corrupting the X/Z position rolls. */
@Mixin(SpawnHelper.class)
public class SpawnHelperMaxPackMixin {
	@Redirect(
			method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I", ordinal = 2))
	private static int worstluck$maxPackSize(Random random, int bound) {
		return bound <= 1 ? 0 : bound - 1;
	}
}
