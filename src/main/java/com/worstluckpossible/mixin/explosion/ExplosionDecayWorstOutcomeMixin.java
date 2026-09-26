package com.worstluckpossible.mixin.explosion;

import net.minecraft.loot.function.ExplosionDecayLootFunction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Makes each explosion-decay roll fail whenever failure is a vanilla possibility. */
@Mixin(ExplosionDecayLootFunction.class)
public abstract class ExplosionDecayWorstOutcomeMixin {
	@Redirect(
			method = "process",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"
			)
	)
	private float worstluck$loseExplosionDrop(Random random) {
		return Math.nextDown(1.0F);
	}
}