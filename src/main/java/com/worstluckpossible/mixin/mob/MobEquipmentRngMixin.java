package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEquipmentRngMixin {
	@Inject(method = "initEquipment", at = @At("TAIL"))
	private void worstluck$giveZombiesDiamondArmor(Random random, LocalDifficulty difficulty, CallbackInfo ci) {
		MobEntity self = (MobEntity) (Object) this;
		if (!(self instanceof ZombieEntity)) {
			return;
		}

		// The old redirect also forced the vanilla "stop adding armor" roll,
		// then filled every empty slot with iron. Give zombies the actual
		// strongest naturally supported armor tier instead.
		self.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
		self.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
		self.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
		self.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
	}

	@Redirect(method = "updateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"), require = 0)
	private float worstluck$alwaysEnchant(Random random) {
		return 0.0F;
	}
}
