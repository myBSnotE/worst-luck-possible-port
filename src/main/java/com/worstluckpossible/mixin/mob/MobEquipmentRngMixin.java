package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MobEntity.class)
public class MobEquipmentRngMixin {
	@Redirect(method = "initEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"), require = 0)
	private float worstluck$alwaysPassEquipmentRoll(Random random) {
		return 0.0F;
	}

	@Redirect(method = "updateEnchantments", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"), require = 0)
	private float worstluck$alwaysEnchant(Random random) {
		return 0.0F;
	}
}
