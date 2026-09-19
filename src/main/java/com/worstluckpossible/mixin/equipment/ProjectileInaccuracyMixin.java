package com.worstluckpossible.mixin.equipment;

import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ProjectileEntity.class)
public class ProjectileInaccuracyMixin {
	@ModifyVariable(method = "setVelocity(DDDFF)V", at = @At("HEAD"), argsOnly = true, index = 8, require = 0)
	private float worstluck$worseAccuracy(float divergence) {
		return Math.max(divergence, 12.0F);
	}
}
