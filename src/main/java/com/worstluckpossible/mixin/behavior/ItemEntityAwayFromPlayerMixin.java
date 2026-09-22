package com.worstluckpossible.mixin.behavior;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Launches fresh block drops away from the nearest player exactly once. */
@Mixin(ItemEntity.class)
public class ItemEntityAwayFromPlayerMixin {
	@Inject(method = "tick", at = @At("HEAD"))
	private void worstluck$launchAwayOnce(CallbackInfo ci) {
		ItemEntity self = (ItemEntity) (Object) this;
		if (self.getEntityWorld().isClient()
				|| self.isRemoved()
				|| self.getItemAge() != 0
				|| self.getOwner() instanceof PlayerEntity) {
			return;
		}

		PlayerEntity player = self.getEntityWorld().getClosestPlayer(self, 64.0);
		if (player == null) {
			return;
		}
		double dx = self.getX() - player.getX();
		double dz = self.getZ() - player.getZ();
		double lengthSq = dx * dx + dz * dz;
		if (lengthSq < 1.0E-4) {
			double angle = self.getRandom().nextDouble() * Math.PI * 2.0;
			dx = Math.cos(angle);
			dz = Math.sin(angle);
			lengthSq = 1.0;
		}
		double length = Math.sqrt(lengthSq);
		self.setVelocity(new Vec3d(dx / length * 0.25, 0.2, dz / length * 0.25));
	}
}
