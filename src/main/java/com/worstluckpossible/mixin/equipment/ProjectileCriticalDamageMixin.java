package com.worstluckpossible.mixin.equipment;

import com.worstluckpossible.config.WorstLuckConfig;
import com.worstluckpossible.config.WorstLuckConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.server.world.ServerWorld;
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
		PersistentProjectileEntity projectile = (PersistentProjectileEntity) (Object) this;
		if (!(projectile.getEntityWorld() instanceof ServerWorld world)
				|| WorstLuckConfigManager.get(world.getServer()).projectileCriticalDamage
				== WorstLuckConfig.ProjectileCriticalDamage.VANILLA) {
			return random.nextInt(bound);
		}
		Entity owner = projectile.getOwner();
		if (owner instanceof PlayerEntity) {
			return 0;
		}
		if (owner instanceof HostileEntity) {
			return Math.max(0, bound - 1);
		}
		return random.nextInt(bound);
	}
}