package com.worstluckpossible.mixin.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Mob armor takes no combat durability damage, without making dropped armor unbreakable. */
@Mixin(ItemStack.class)
public class MobArmorDurabilityMixin {
	@Inject(method = "damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V",
			at = @At("HEAD"), cancellable = true)
	private void worstluck$protectMobArmor(int amount, LivingEntity entity, EquipmentSlot slot, CallbackInfo ci) {
		if (entity instanceof MobEntity && slot.isArmorSlot()) {
			ci.cancel();
		}
	}
}
