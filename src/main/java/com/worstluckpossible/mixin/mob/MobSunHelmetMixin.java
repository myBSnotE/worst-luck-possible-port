package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Sunlight never consumes a mob's helmet durability. */
@Mixin(MobEntity.class)
public class MobSunHelmetMixin {
	@Redirect(method = "tickBurnInDaylight", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"))
	private void worstluck$keepSunHelmetIntact(ItemStack stack, int damage) {
		// Intentionally keep the existing damage value.
	}
}
