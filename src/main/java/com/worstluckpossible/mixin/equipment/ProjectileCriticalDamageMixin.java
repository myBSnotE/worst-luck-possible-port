package com.worstluckpossible.mixin.equipment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Chooses the least favorable critical-projectile damage bonus for its owner. */
@Mixin(PersistentProjectileEntity.class)
public abstract class ProjectileCriticalDamageMixin {
	@Redirect(
			method = "onEntityHit",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"
			)
	)
	private int worstluck$chooseCriticalDamageBonus(Random random, int bound) {
		Entity owner = ((PersistentProjectileEntity) (Object) this).getOwner();
		if (owner instanceof PlayerEntity) {
			return 0;
		}
		if (owner instanceof HostileEntity) {
			return Math.max(0, bound - 1);
		}
		return random.nextInt(bound);
	}
}