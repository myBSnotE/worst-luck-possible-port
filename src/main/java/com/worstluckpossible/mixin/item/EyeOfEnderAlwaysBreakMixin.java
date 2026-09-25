package com.worstluckpossible.mixin.item;

import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Every thrown Eye of Ender chooses vanilla's 20% break outcome. */
@Mixin(EyeOfEnderEntity.class)
public class EyeOfEnderAlwaysBreakMixin {
	@Shadow private boolean dropsItem;

	@Inject(method = "initTargetPos", at = @At("TAIL"))
	private void worstluck$alwaysBreak(Vec3d pos, CallbackInfo ci) {
		this.dropsItem = false;
	}
}
