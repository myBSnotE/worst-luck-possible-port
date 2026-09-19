package com.worstluckpossible.mixin.behavior;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Dropped items actively slide away from nearby players, making pickups harder. */
@Mixin(ItemEntity.class)
public class ItemEntityAwayFromPlayerMixin {
	private static final double WORSTLUCK_RANGE = 8.0;
	private static final double WORSTLUCK_ACCELERATION = 0.08;
	private static final double WORSTLUCK_MAX_HORIZONTAL_SPEED = 0.45;

	@Inject(method = "tick", at = @At("TAIL"))
	private void worstluck$moveAwayFromPlayer(CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;
		if (self.getEntityWorld().isClient() || self.isRemoved()) {
			return;
		}

		PlayerEntity player = self.getEntityWorld().getClosestPlayer(self, WORSTLUCK_RANGE);
		if (player == null) {
			return;
		}

		double dx = self.getX() - player.getX();
		double dz = self.getZ() - player.getZ();
		double distanceSquared = dx * dx + dz * dz;
		if (distanceSquared < 1.0E-6) {
			dx = self.getRandom().nextBoolean() ? 1.0 : -1.0;
			dz = self.getRandom().nextBoolean() ? 1.0 : -1.0;
			distanceSquared = 2.0;
		}

		double scale = WORSTLUCK_ACCELERATION / Math.sqrt(distanceSquared);
		Vec3d velocity = self.getVelocity().add(dx * scale, 0.0, dz * scale);
		double horizontalSpeedSquared = velocity.x * velocity.x + velocity.z * velocity.z;
		if (horizontalSpeedSquared > WORSTLUCK_MAX_HORIZONTAL_SPEED * WORSTLUCK_MAX_HORIZONTAL_SPEED) {
			double horizontalScale = WORSTLUCK_MAX_HORIZONTAL_SPEED / Math.sqrt(horizontalSpeedSquared);
			velocity = new Vec3d(velocity.x * horizontalScale, velocity.y, velocity.z * horizontalScale);
		}

		self.setVelocity(velocity);
	}
}
