package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.mob.SlimeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Slimes use the minimum vanilla random jump delay; magma cubes retain their vanilla 4x multiplier. */
@Mixin(SlimeEntity.class)
public class SlimeWorstJumpMixin {
	@Inject(method = "getTicksUntilNextJump", at = @At("HEAD"), cancellable = true)
	private void worstluck$minimumJumpDelay(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(10);
	}
}
