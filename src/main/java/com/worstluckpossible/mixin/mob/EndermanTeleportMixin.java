package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Redirects vanilla random teleports 10-50 blocks along the player's look vector. */
@Mixin(EndermanEntity.class)
public abstract class EndermanTeleportMixin {
	@Invoker("teleportTo")
	abstract boolean worstluck$teleportTo(double x, double y, double z);

	@Inject(method = "teleportRandomly", at = @At("HEAD"), cancellable = true)
	private void worstluck$teleportWherePlayerLooks(CallbackInfoReturnable<Boolean> cir) {
		EndermanEntity self = (EndermanEntity) (Object) this;
		if (self.getEntityWorld().isClient()) {
			return;
		}
		LivingEntity target = self.getTarget();
		PlayerEntity player = target instanceof PlayerEntity p
				? p : self.getEntityWorld().getClosestPlayer(self, 64.0);
		if (player == null || player.isSpectator()) {
			cir.setReturnValue(false);
			return;
		}
		Vec3d look = player.getRotationVec(1.0F);
		Vec3d eye = player.getEyePos();
		for (int attempt = 0; attempt < 16; attempt++) {
			double distance = 10.0 + self.getRandom().nextDouble() * 40.0;
			if (worstluck$teleportTo(
					eye.x + look.x * distance,
					eye.y + look.y * distance,
					eye.z + look.z * distance)) {
				cir.setReturnValue(true);
				return;
			}
		}
		cir.setReturnValue(false);
	}
}
