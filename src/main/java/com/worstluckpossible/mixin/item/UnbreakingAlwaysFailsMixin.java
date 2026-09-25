package com.worstluckpossible.mixin.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Unbreaking always chooses to consume durability. */
@Mixin(EnchantmentHelper.class)
public class UnbreakingAlwaysFailsMixin {
	@Inject(method = "getItemDamage", at = @At("HEAD"), cancellable = true)
	private static void worstluck$ignoreDurabilityProtection(ServerWorld world, ItemStack stack,
			int baseItemDamage, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(baseItemDamage);
	}
}
