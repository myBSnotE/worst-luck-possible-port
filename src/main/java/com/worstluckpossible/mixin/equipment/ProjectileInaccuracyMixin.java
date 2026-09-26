package com.worstluckpossible.mixin.equipment;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Chooses a maximum-angle result from vanilla's per-axis projectile uncertainty cube. */
@Mixin(ProjectileEntity.class)
public class ProjectileInaccuracyMixin {
	private static final double VANILLA_UNCERTAINTY_SCALE = 0.0172275;
	private static final double TIE_EPSILON = 1.0E-12;

	@Inject(method = "setVelocity(DDDFF)V", at = @At("TAIL"))
	private void worstluck$useMaximumVanillaSpread(double x, double y, double z, float power,
			float uncertainty, CallbackInfo ci) {
		ProjectileEntity self = (ProjectileEntity) (Object) this;
		if (!(self.getOwner() instanceof PlayerEntity) || uncertainty <= 0.0F) {
			return;
		}

		Vec3d aim = new Vec3d(x, y, z);
		if (aim.lengthSquared() == 0.0) {
			return;
		}
		aim = aim.normalize();
		double offset = VANILLA_UNCERTAINTY_SCALE * uncertainty;
		double lowestDot = Double.POSITIVE_INFINITY;
		List<Vec3d> worstCandidates = new ArrayList<>(4);

		for (int sx : new int[]{-1, 1}) {
			for (int sy : new int[]{-1, 1}) {
				for (int sz : new int[]{-1, 1}) {
					Vec3d candidate = aim.add(sx * offset, sy * offset, sz * offset);
					double dot = aim.dotProduct(candidate.normalize());
					if (dot < lowestDot - TIE_EPSILON) {
						lowestDot = dot;
						worstCandidates.clear();
						worstCandidates.add(candidate);
					} else if (Math.abs(dot - lowestDot) <= TIE_EPSILON) {
						worstCandidates.add(candidate);
					}
				}
			}
		}

		Vec3d chosen = worstCandidates.get(self.getRandom().nextInt(worstCandidates.size())).multiply(power);
		self.setVelocity(chosen);
	}
}
