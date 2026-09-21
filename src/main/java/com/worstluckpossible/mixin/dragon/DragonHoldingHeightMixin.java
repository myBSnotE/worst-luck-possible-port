package com.worstluckpossible.mixin.dragon;

import net.minecraft.entity.boss.dragon.phase.HoldingPatternPhase;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HoldingPatternPhase.class)
public class DragonHoldingHeightMixin {
	@Redirect(method = "followPath", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"))
	private float worstluck$highestHoldingPath(Random random) {
		return 0.999999F;
	}
}
