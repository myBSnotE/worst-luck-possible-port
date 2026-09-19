package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndermanEntity.class)
public abstract class EndermanTeleportMixin {
	@Invoker("teleportTo")
	abstract boolean worstluck$teleportTo(double x, double y, double z);

	@Inject(method = "teleportRandomly", at = @At("HEAD"), cancellable = true, require = 0)
	private void worstluck$teleportOntoPlayer(CallbackInfoReturnable<Boolean> cir) {
		EndermanEntity self = (EndermanEntity) (Object) this;
		if (self.getEntityWorld().isClient()) {
			return;
		}
		PlayerEntity player = self.getEntityWorld().getClosestPlayer(self, 64.0);
		if (player == null) {
			return;
		}
		Vec3d target = player.getEntityPos().add(player.getRotationVec(1.0F).multiply(2.0));
		cir.setReturnValue(this.worstluck$teleportTo(target.x, target.y, target.z));
	}
}
