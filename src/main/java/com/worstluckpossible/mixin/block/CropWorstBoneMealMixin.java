package com.worstluckpossible.mixin.block;

import net.minecraft.block.CropBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Bonemealed crops always gain the minimum vanilla two growth stages. */
@Mixin(CropBlock.class)
public class CropWorstBoneMealMixin {
	@Inject(method = "getGrowthAmount", at = @At("HEAD"), cancellable = true)
	private void worstluck$minimumCropGrowth(World world, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(2);
	}
}
