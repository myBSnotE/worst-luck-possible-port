package com.worstluckpossible.mixin.equipment;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Chooses a varied maximum-boundary result from vanilla's per-axis uncertainty cube. */
@Mixin(ProjectileEntity.class)
public class ProjectileInaccuracyMixin {
	private static final double VANILLA_UNCERTAINTY_SCALE = 0.0172275;

	@Inject(method = "setVelocity(DDDFF)V", at = @At("TAIL"))
	private void worstluck$useVariedMaximumVanillaSpread(double x, double y, double z, float power,
			float uncertainty, CallbackInfo ci) {
		ProjectileEntity self = (ProjectileEntity) (Object) this;
		if (!(self.getOwner() instanceof PlayerEntity) || uncertainty <= 0.0F) {
			return;
		}

		Vec3d aim = new Vec3d(x, y, z);
		if (aim.lengthSquared() == 0.0D) {
			return;
		}
		aim = aim.normalize();

		// Pick a random azimuth around the aim direction, then extend the perpendicular
		// error until it touches one face of vanilla's per-axis uncertainty cube. This
		// keeps every component inside the vanilla bound while avoiding the former
		// deterministic single corner for a fixed camera direction.
		Vec3d reference = Math.abs(aim.y) < 0.999D ? new Vec3d(0.0D, 1.0D, 0.0D) : new Vec3d(1.0D, 0.0D, 0.0D);
		Vec3d firstAxis = aim.crossProduct(reference).normalize();
		Vec3d secondAxis = aim.crossProduct(firstAxis).normalize();
		double azimuth = self.getRandom().nextDouble() * Math.PI * 2.0D;
		Vec3d perpendicular = firstAxis.multiply(Math.cos(azimuth)).add(secondAxis.multiply(Math.sin(azimuth)));

		double offset = VANILLA_UNCERTAINTY_SCALE * uncertainty;
		double largestComponent = Math.max(Math.abs(perpendicular.x), Math.max(Math.abs(perpendicular.y), Math.abs(perpendicular.z)));
		if (largestComponent == 0.0D) {
			return;
		}

		Vec3d error = perpendicular.multiply(offset / largestComponent);
		Vec3d chosen = aim.add(error).normalize().multiply(power);
		self.setVelocity(chosen);
	}
}
