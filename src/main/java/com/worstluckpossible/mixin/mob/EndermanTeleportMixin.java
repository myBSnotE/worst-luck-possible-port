package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
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
		if (player != null && !player.isSpectator() && !player.isCreative()) {
			cir.setReturnValue(worstluck$teleportNextTo(player));
		}
	}

	@Inject(method = "mobTick", at = @At("TAIL"))
	private void worstluck$periodicallyTeleportToPlayer(ServerWorld world, CallbackInfo ci) {
		EndermanEntity self = (EndermanEntity) (Object) this;
		if ((self.age + self.getId()) % 10 != 0) {
			return;
		}

		PlayerEntity player = world.getClosestPlayer(self, 64.0);
		if (player != null
				&& !player.isSpectator()
				&& !player.isCreative()
				&& self.squaredDistanceTo(player) > 6.25) {
			worstluck$teleportNextTo(player);
		}
	}

	private boolean worstluck$teleportNextTo(PlayerEntity player) {
		// Try a ring of valid positions around the player. One fixed position can
		// be blocked by the player, a wall, water or a height difference.
		for (double radius : new double[] {1.75, 2.25, 2.75}) {
			for (int i = 0; i < 12; i++) {
				double angle = i * Math.PI * 2.0 / 12.0;
				double x = player.getX() + Math.cos(angle) * radius;
				double z = player.getZ() + Math.sin(angle) * radius;
				if (worstluck$teleportTo(x, player.getY(), z)) {
					return true;
				}
			}
		}
		return false;
	}
}
