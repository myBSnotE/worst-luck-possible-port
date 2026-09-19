package com.worstluckpossible.mixin.mob;

import java.util.List;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PiglinBrain.class)
public class PiglinBarterMixin {
	@Inject(method = "getBarteredItem", at = @At("HEAD"), cancellable = true, require = 0)
	private static void worstluck$worstBarter(PiglinEntity piglin, CallbackInfoReturnable<List<ItemStack>> cir) {
		cir.setReturnValue(List.of(new ItemStack(Items.GRAVEL)));
	}
}
