package com.worstluckpossible.mixin.item;

import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Thrown eggs always choose vanilla's no-chick result. */
@Mixin(EggEntity.class)
public class EggNoChicksMixin {
	@Redirect(method = "onCollision", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I", ordinal = 0))
	private int worstluck$neverHatch(Random random, int bound) {
		return 1;
	}
}
