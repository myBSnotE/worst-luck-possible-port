package com.worstluckpossible.mixin.spawns;

import net.minecraft.util.math.random.Random;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Every random roll while spawning a pack returns its maximum, so groups are always as large as allowed. */
@Mixin(SpawnHelper.class)
public class SpawnHelperMaxPackMixin {
	@Redirect(
			method = "spawnEntitiesInChunk",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"),
			require = 0,
			expect = 0)
	private static int worstluck$maxRoll(Random random, int bound) {
		return bound <= 1 ? 0 : bound - 1;
	}
}
