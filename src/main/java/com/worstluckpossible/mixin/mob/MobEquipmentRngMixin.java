package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Reproduces the original maximum vanilla equipment rolls without hard-coded armor. */
@Mixin(MobEntity.class)
public class MobEquipmentRngMixin {
	@Unique private int worstluck$equipmentFloatRoll;

	@Inject(method = "initEquipment", at = @At("HEAD"))
	private void worstluck$resetEquipmentRolls(Random random, LocalDifficulty difficulty, CallbackInfo ci) {
		worstluck$equipmentFloatRoll = 0;
	}

	@Redirect(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"))
	private float worstluck$maximizeEquipment(Random random) {
		return worstluck$equipmentFloatRoll++ < 4 ? 0.0F : 1.0F;
	}

	@Redirect(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
	private int worstluck$highestBaseTier(Random random, int bound) {
		return Math.max(0, bound - 1);
	}

	@Redirect(
		method = "enchantEquipment(Lnet/minecraft/world/ServerWorldAccess;Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/util/math/random/Random;FLnet/minecraft/world/LocalDifficulty;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"),
		require = 0
	)
	private float worstluck$alwaysEnchant(Random random) {
		return 0.0F;
	}
}
