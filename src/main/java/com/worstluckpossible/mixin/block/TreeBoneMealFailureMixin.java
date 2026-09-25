package com.worstluckpossible.mixin.block;

import net.minecraft.block.AzaleaBlock;
import net.minecraft.block.FungusBlock;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Tree-like bonemeal targets always choose their vanilla random failure outcome. */
@Mixin({SaplingBlock.class, AzaleaBlock.class, FungusBlock.class, MushroomPlantBlock.class})
public class TreeBoneMealFailureMixin {
	@Inject(method = "canGrow", at = @At("HEAD"), cancellable = true)
	private void worstluck$failRandomBonemealGrowth(World world, Random random, BlockPos pos,
			net.minecraft.block.BlockState state, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}
}
