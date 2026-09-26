package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** Keeps the vanilla item chance, but makes a successful fox item roll an egg. */
@Mixin(FoxEntity.class)
public abstract class FoxWorstSpawnItemMixin {
	@ModifyArg(
			method = "initEquipment",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/passive/FoxEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V"
			),
			index = 1
	)
	private ItemStack worstluck$replaceSpawnItemWithEgg(ItemStack original) {
		return new ItemStack(Items.EGG);
	}
}