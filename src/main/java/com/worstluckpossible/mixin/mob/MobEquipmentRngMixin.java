package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
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
	@Redirect(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"), require = 0)
	private float worstluck$alwaysPassEquipmentRoll(Random random) {
		return 0.0F;
	}

	@Inject(method = "initEquipment", at = @At("TAIL"))
	private void worstluck$guaranteeMonsterArmor(Random random, LocalDifficulty difficulty, CallbackInfo ci) {
		MobEntity self = (MobEntity) (Object) this;
		if (self.getType().getSpawnGroup() != SpawnGroup.MONSTER) {
			return;
		}

		worstluck$equipIfEmpty(self, EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
		worstluck$equipIfEmpty(self, EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
		worstluck$equipIfEmpty(self, EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
		worstluck$equipIfEmpty(self, EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
	}

	private static void worstluck$equipIfEmpty(MobEntity mob, EquipmentSlot slot, ItemStack stack) {
		if (mob.getEquippedStack(slot).isEmpty()) {
			mob.equipStack(slot, stack);
		}
	}

	@Redirect(method = "updateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"), require = 0)
	private float worstluck$alwaysEnchant(Random random) {
		return 0.0F;
	}
}
