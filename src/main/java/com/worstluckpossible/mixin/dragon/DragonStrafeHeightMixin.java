package com.worstluckpossible.mixin.dragon;

import net.minecraft.entity.boss.dragon.phase.StrafePlayerPhase;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** The dragon picks the lowest possible strafe path instead of adding up to 20 random blocks of height. */
@Mixin(StrafePlayerPhase.class)
public class DragonStrafeHeightMixin {
	@Redirect(
			method = "followPath",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"))
	private float worstluck$lowestStrafePath(Random random) {
		return 0.0F;
	}
}
