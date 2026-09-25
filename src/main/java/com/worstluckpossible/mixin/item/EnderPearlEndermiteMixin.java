package com.worstluckpossible.mixin.item;

import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** Every successful player pearl teleport chooses vanilla's endermite outcome. */
@Mixin(EnderPearlEntity.class)
public class EnderPearlEndermiteMixin {
	@ModifyConstant(method = "onCollision", constant = @Constant(floatValue = 0.05F))
	private float worstluck$guaranteeEndermite(float original) {
		return 1.0F;
	}
}
