package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.registry.tag.FluidTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * A drowned chicken jockey can be created below the water surface because
 * drowned spawn in water. Vanilla immediately dismounts a living passenger
 * whose vehicle is tagged to dismount underwater, leaving the drowned to sink
 * while the chicken floats away.
 *
 * Keep a newly spawned drowned mounted until its head reaches air for the first
 * time. After that, the normal vanilla rule is restored: if the rider's head
 * becomes submerged again, it dismounts from the chicken.
 */
@Mixin(LivingEntity.class)
public abstract class DrownedChickenJockeyMixin {
	@Unique
	private boolean worstluck$chickenJockeyReachedSurface;

	@Inject(method = "baseTick", at = @At("HEAD"), require = 1)
	private void worstluck$trackChickenJockeySurface(CallbackInfo ci) {
		LivingEntity self = (LivingEntity) (Object) this;
		Entity vehicle = self.getVehicle();

		if (!(self instanceof DrownedEntity) || !(vehicle instanceof ChickenEntity)) {
			worstluck$chickenJockeyReachedSurface = false;
			return;
		}

		if (!self.isSubmergedIn(FluidTags.WATER)) {
			worstluck$chickenJockeyReachedSurface = true;
		}
	}

	@Redirect(method = "baseTick", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/Entity;shouldDismountUnderwater()Z"), require = 1)
	private boolean worstluck$delayDrownedChickenDismount(Entity vehicle) {
		LivingEntity self = (LivingEntity) (Object) this;
		if (self instanceof DrownedEntity
				&& vehicle instanceof ChickenEntity
				&& !worstluck$chickenJockeyReachedSurface) {
			return false;
		}

		return vehicle.shouldDismountUnderwater();
	}
}
