package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Tamed cats can still sleep beside their owner, but never win the morning-gift roll. */
@Mixin(targets = "net.minecraft.entity.passive.CatEntity$SleepWithOwnerGoal")
public class CatNoMorningGiftMixin {
	@Redirect(method = "stop", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"))
	private float worstluck$neverGiveMorningGift(Random random) {
		return 1.0F;
	}
}
