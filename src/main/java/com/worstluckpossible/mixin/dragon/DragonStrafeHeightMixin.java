package com.worstluckpossible.mixin.dragon;

import net.minecraft.entity.boss.dragon.phase.StrafePlayerPhase;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Forces both strafe paths and player approaches to their maximum height. */
@Mixin(StrafePlayerPhase.class)
public class DragonStrafeHeightMixin {
	@Redirect(method = "followPath", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"))
	private float worstluck$highestStrafePath(Random random) {
		return 0.999999F;
	}

	@ModifyArg(method = {"serverTick", "setTargetEntity"}, at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(DD)D"), index = 0, require = 0)
	private double worstluck$maximumApproachHeight(double calculatedOffset) {
		return 10.0;
	}
}
