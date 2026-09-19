package com.worstluckpossible.mixin.behavior;

import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WanderAroundGoal.class)
public class WanderTowardPlayerMixin {
	@Shadow
	protected PathAwareEntity mob;

	@Inject(method = "getWanderTarget", at = @At("HEAD"), cancellable = true, require = 0)
	private void worstluck$wanderTowardPlayer(CallbackInfoReturnable<Vec3d> cir) {
		PlayerEntity player = this.mob.getEntityWorld().getClosestPlayer(this.mob, 64.0);
		if (player == null) {
			return;
		}
		Vec3d target = NoPenaltyTargeting.findTo(this.mob, 10, 7, player.getEntityPos(), Math.PI / 2.0);
		if (target != null) {
			cir.setReturnValue(target);
		}
	}
}
