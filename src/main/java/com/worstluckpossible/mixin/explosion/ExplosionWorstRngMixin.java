package com.worstluckpossible.mixin.explosion;

import net.minecraft.util.math.random.Random;
import net.minecraft.world.explosion.ExplosionImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Selects the most destructive runtime-random outcomes for explosions. */
@Mixin(ExplosionImpl.class)
public abstract class ExplosionWorstRngMixin {
	@Redirect(
			method = "getBlocksToDestroy",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"
			)
	)
	private float worstluck$maximumRayStrength(Random random) {
		return Math.nextDown(1.0F);
	}

	@Redirect(
			method = "createFire",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"
			)
	)
	private int worstluck$guaranteeExplosionFire(Random random, int bound) {
		return 0;
	}
}