package com.worstluckpossible.mixin.behavior;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Makes persistent hostile mobs actively converge on nearby players. */
@Mixin(MobEntity.class)
public class WanderTowardPlayerMixin {
	private static final double WORSTLUCK_PURSUIT_RANGE = 256.0;
	private static final double WORSTLUCK_PURSUIT_SPEED = 1.15;

	@Inject(method = "mobTick", at = @At("TAIL"))
	private void worstluck$pursueNearestPlayer(ServerWorld world, CallbackInfo ci) {
		MobEntity self = (MobEntity) (Object) this;
		if (self.getType().getSpawnGroup() != SpawnGroup.MONSTER
				|| self.isAiDisabled()
				|| self.hasVehicle()
				|| (self.age + self.getId()) % 20 != 0) {
			return;
		}

		PlayerEntity player = world.getClosestPlayer(self, WORSTLUCK_PURSUIT_RANGE);
		if (player == null || player.isSpectator() || player.isCreative()) {
			return;
		}

		self.setTarget(player);
		if (self.squaredDistanceTo(player) > 4.0) {
			self.getNavigation().startMovingTo(player, WORSTLUCK_PURSUIT_SPEED);
		}
	}
}
