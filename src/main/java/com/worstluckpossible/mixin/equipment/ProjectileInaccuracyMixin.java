package com.worstluckpossible.mixin.equipment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Applies independent uniform [-pi, pi] yaw and pitch offsets to player projectiles. */
@Mixin(ProjectileEntity.class)
public class ProjectileInaccuracyMixin {
	@Inject(method = "setVelocity(DDDFF)V", at = @At("TAIL"))
	private void worstluck$randomizeDirection(double x, double y, double z, float power, float uncertainty, CallbackInfo ci) {
		ProjectileEntity self = (ProjectileEntity) (Object) this;
		if (!(self.getOwner() instanceof PlayerEntity)) {
			return;
		}
		Vec3d velocity = self.getVelocity();
		double speed = velocity.length();
		if (speed == 0.0) {
			return;
		}
		double yaw = Math.atan2(velocity.z, velocity.x) + uniformOffset(self);
		double pitch = Math.asin(velocity.y / speed) + uniformOffset(self);
		double cosPitch = Math.cos(pitch);
		self.setVelocity(speed * cosPitch * Math.cos(yaw), speed * Math.sin(pitch), speed * cosPitch * Math.sin(yaw));
	}

	private static double uniformOffset(Entity entity) {
		return (entity.getRandom().nextDouble() * 2.0 - 1.0) * Math.PI;
	}
}
