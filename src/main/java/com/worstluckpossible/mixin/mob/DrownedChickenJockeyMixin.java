package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.passive.ChickenEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Drowned chicken jockeys can naturally be created below the water surface.
 * Vanilla normally makes living passengers dismount vehicles such as chickens
 * when the passenger's head is submerged, immediately separating this pair.
 * Keep drowned riders mounted so the chicken can carry them toward the surface.
 */
@Mixin(LivingEntity.class)
public abstract class DrownedChickenJockeyMixin {
	@Redirect(method = "baseTick", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/Entity;shouldDismountUnderwater()Z"), require = 1)
	private boolean worstluck$keepDrownedMountedUnderwater(Entity vehicle) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self instanceof DrownedEntity && vehicle instanceof ChickenEntity) {
			return false;
		}

		return vehicle.shouldDismountUnderwater();
	}
}
