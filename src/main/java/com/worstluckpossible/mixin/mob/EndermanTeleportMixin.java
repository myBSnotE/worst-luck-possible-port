package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndermanTeleportMixin {
	@Invoker("teleportTo")
	abstract boolean worstluck$teleportTo(double x, double y, double z);

	@Inject(method = "teleportRandomly", at = @At("HEAD"), cancellable = true)
	private void worstluck$teleportOntoPlayer(CallbackInfoReturnable<Boolean> cir) {
		EndermanEntity self = (EndermanEntity) (Object) this;
		if (self.getEntityWorld().isClient()) {
			return;
		}
		PlayerEntity player = self.getEntityWorld().getClosestPlayer(self, 64.0);
		if (player != null) {
			cir.setReturnValue(worstluck$teleportBeside(self, player));
		}
	}

	@Inject(method = "mobTick", at = @At("TAIL"))
	private void worstluck$periodicallyTeleportToTarget(ServerWorld world, CallbackInfo ci) {
		EndermanEntity self = (EndermanEntity) (Object) this;
		LivingEntity target = self.getTarget();
		if (target instanceof PlayerEntity player
				&& self.age % 20 == 0
				&& self.squaredDistanceTo(player) > 4.0) {
			worstluck$teleportBeside(self, player);
		}
	}

	private boolean worstluck$teleportBeside(EndermanEntity self, PlayerEntity player) {
		Vec3d away = self.getEntityPos().subtract(player.getEntityPos());
		if (away.horizontalLengthSquared() < 1.0E-6) {
			away = player.getRotationVec(1.0F).multiply(-1.0);
		}
		away = new Vec3d(away.x, 0.0, away.z).normalize().multiply(1.5);
		Vec3d target = player.getEntityPos().add(away);
		return this.worstluck$teleportTo(target.x, target.y, target.z);
	}
}
