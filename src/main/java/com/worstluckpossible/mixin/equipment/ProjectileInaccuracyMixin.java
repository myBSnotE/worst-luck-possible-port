package com.worstluckpossible.mixin.equipment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Chooses the least favorable valid result from vanilla's per-axis uncertainty cube. */
@Mixin(ProjectileEntity.class)
public class ProjectileInaccuracyMixin {
	private static final double VANILLA_UNCERTAINTY_SCALE = 0.0172275;
	private static final double PROJECTILE_DRAG = 0.99;
	private static final int MAX_PREDICTION_TICKS = 60;
	private static final int AIM_SEARCH_STEPS = 256;

	@Inject(method = "setVelocity(DDDFF)V", at = @At("TAIL"))
	private void worstluck$chooseWorstVanillaSpread(double x, double y, double z, float power,
			float uncertainty, CallbackInfo ci) {
		ProjectileEntity self = (ProjectileEntity) (Object) this;
		if (uncertainty <= 0.0F || power <= 0.0F) {
			return;
		}

		Vec3d aim = new Vec3d(x, y, z);
		if (aim.lengthSquared() == 0.0D) {
			return;
		}
		aim = aim.normalize();

		Entity owner = self.getOwner();
		if (owner instanceof HostileEntity hostile && hostile.getTarget() instanceof PlayerEntity target
				&& target.isAlive() && !target.isSpectator()) {
			// ServerPlayerEntity#getMovement is updated from accepted movement packets and
			// therefore reflects real player displacement on a dedicated server.
			Vec3d predictedAim = worstluck$predictAim(new Vec3d(x, y, z), target.getMovement(), power);
			double bound = Math.nextDown(VANILLA_UNCERTAINTY_SCALE * uncertainty);
			Vec3d chosen = worstluck$closestAllowedDirection(aim, predictedAim, bound).multiply(power);
			self.setVelocity(chosen);
			return;
		}

		if (!(owner instanceof PlayerEntity)) {
			return;
		}

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

	private static Vec3d worstluck$predictAim(Vec3d originalAim, Vec3d targetVelocity, float power) {
		Vec3d velocity = targetVelocity;
		double speed = velocity.length();
		if (speed > 1.5D) {
			velocity = velocity.multiply(1.5D / speed);
		}

		double flightTicks = worstluck$estimateFlightTicks(originalAim, power);
		Vec3d predicted = originalAim;
		for (int iteration = 0; iteration < 4; iteration++) {
			predicted = originalAim.add(velocity.multiply(flightTicks));
			flightTicks = worstluck$estimateFlightTicks(predicted, power);
		}
		return predicted.normalize();
	}

	private static double worstluck$estimateFlightTicks(Vec3d displacement, float power) {
		double horizontalDistance = Math.sqrt(displacement.x * displacement.x + displacement.z * displacement.z);
		double horizontalFraction = displacement.normalize().horizontalLength();
		double horizontalSpeed = Math.max(1.0E-4D, power * horizontalFraction);
		double travelled = 0.0D;
		double tickSpeed = horizontalSpeed;
		int bestTick = 1;
		double bestError = Double.MAX_VALUE;
		for (int tick = 1; tick <= MAX_PREDICTION_TICKS; tick++) {
			travelled += tickSpeed;
			tickSpeed *= PROJECTILE_DRAG;
			double error = Math.abs(travelled - horizontalDistance);
			if (error < bestError) {
				bestError = error;
				bestTick = tick;
			}
		}
		return bestTick;
	}

	/**
	 * Finds the direction inside vanilla's per-axis uncertainty cube that has the
	 * smallest angle to the predicted intercept direction. Sampling scale along
	 * the desired ray also handles cases where the exact direction cannot fit.
	 */
	private static Vec3d worstluck$closestAllowedDirection(Vec3d vanillaAim, Vec3d desiredAim, double bound) {
		if (bound <= 0.0D || desiredAim.lengthSquared() == 0.0D) {
			return vanillaAim;
		}

		Vec3d best = vanillaAim;
		double bestAlignment = best.dotProduct(desiredAim);
		for (int step = 0; step <= AIM_SEARCH_STEPS; step++) {
			double scale = 0.25D + 1.5D * step / AIM_SEARCH_STEPS;
			Vec3d pointOnDesiredRay = desiredAim.multiply(scale);
			Vec3d candidate = new Vec3d(
					MathHelper.clamp(pointOnDesiredRay.x, vanillaAim.x - bound, vanillaAim.x + bound),
					MathHelper.clamp(pointOnDesiredRay.y, vanillaAim.y - bound, vanillaAim.y + bound),
					MathHelper.clamp(pointOnDesiredRay.z, vanillaAim.z - bound, vanillaAim.z + bound));
			if (candidate.lengthSquared() == 0.0D) {
				continue;
			}
			candidate = candidate.normalize();
			double alignment = candidate.dotProduct(desiredAim);
			if (alignment > bestAlignment) {
				bestAlignment = alignment;
				best = candidate;
			}
		}
		return best;
	}
}
