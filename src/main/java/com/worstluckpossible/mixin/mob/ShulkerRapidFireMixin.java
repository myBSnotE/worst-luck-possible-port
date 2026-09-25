package com.worstluckpossible.mixin.mob;

import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Shulkers choose the minimum vanilla random interval between bullets. */
@Mixin(targets = "net.minecraft.entity.mob.ShulkerEntity$ShootBulletGoal")
public class ShulkerRapidFireMixin {
	@Redirect(method = "tick", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
	private int worstluck$minimumBulletDelay(Random random, int bound) {
		return 0;
	}
}
