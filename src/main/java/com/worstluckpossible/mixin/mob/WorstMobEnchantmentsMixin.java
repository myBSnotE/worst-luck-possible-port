package com.worstluckpossible.mixin.mob;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Replaces random mob enchantments with maximum vanilla-compatible combat enchantments. */
@Mixin(MobEntity.class)
public class WorstMobEnchantmentsMixin {
	@Unique private boolean worstluck$combatEnchantmentsApplied;

	@Inject(method = "tick", at = @At("HEAD"))
	private void worstluck$applyCombatEnchantments(CallbackInfo ci) {
		if (worstluck$combatEnchantmentsApplied) {
			return;
		}
		MobEntity mob = (MobEntity) (Object) this;
		if (!(mob.getEntityWorld() instanceof ServerWorld world) || mob.age < 1) {
			return;
		}
		worstluck$combatEnchantmentsApplied = true;

		for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
			ItemStack armor = mob.getEquippedStack(slot);
			if (!armor.isEmpty()) {
				worstluck$resetEnchantments(armor);
				worstluck$add(world, armor, Enchantments.PROTECTION, 4);
				worstluck$add(world, armor, Enchantments.THORNS, 3);
			}
		}

		ItemStack weapon = mob.getMainHandStack();
		if (weapon.isEmpty()) {
			return;
		}
		worstluck$resetEnchantments(weapon);
		if (weapon.isOf(Items.BOW)) {
			worstluck$add(world, weapon, Enchantments.POWER, 5);
			worstluck$add(world, weapon, Enchantments.PUNCH, 2);
			worstluck$add(world, weapon, Enchantments.FLAME, 1);
		} else if (weapon.isOf(Items.CROSSBOW)) {
			worstluck$add(world, weapon, Enchantments.QUICK_CHARGE, 3);
			worstluck$add(world, weapon, Enchantments.PIERCING, 4);
		} else if (weapon.isIn(ItemTags.SWORDS)) {
			worstluck$add(world, weapon, Enchantments.SHARPNESS, 5);
			worstluck$add(world, weapon, Enchantments.FIRE_ASPECT, 2);
			worstluck$add(world, weapon, Enchantments.KNOCKBACK, 2);
		} else if (weapon.isIn(ItemTags.AXES)) {
			worstluck$add(world, weapon, Enchantments.SHARPNESS, 5);
		} else if (weapon.isOf(Items.TRIDENT)) {
			worstluck$add(world, weapon, Enchantments.IMPALING, 5);
			worstluck$add(world, weapon, Enchantments.LOYALTY, 3);
			worstluck$add(world, weapon, Enchantments.CHANNELING, 1);
		}
	}

	@Unique
	private static void worstluck$resetEnchantments(ItemStack stack) {
		stack.set(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
	}

	@Unique
	private static void worstluck$add(ServerWorld world, ItemStack stack, RegistryKey<Enchantment> key, int level) {
		RegistryEntry<Enchantment> enchantment = world.getRegistryManager()
				.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(key);
		stack.addEnchantment(enchantment, level);
	}
}
