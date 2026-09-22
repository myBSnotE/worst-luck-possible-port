package com.worstluckpossible.mixin.spawns;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Keeps the original mod's protected near-player area while ensuring distant
 * hostiles eventually leave the mob cap. The hard cutoff intentionally matches
 * vanilla's 128-block immediate despawn range so platforms farther than 64
 * blocks from the player remain populated.
 */
@Mixin(MobEntity.class)
public class MobNeverDespawnMixin {
	@Inject(method = "checkDespawn", at = @At("HEAD"), cancellable = true)
	private void worstluck$despawnByDistance(CallbackInfo ci) {
		MobEntity self = (MobEntity) (Object) this;
		if (self.getType().getSpawnGroup() != SpawnGroup.MONSTER
				|| self.isPersistent()
				|| self.cannotDespawn()) {
			return;
		}

		Entity closest = self.getEntityWorld().getClosestPlayer(self, -1.0D);
		if (closest == null) {
			return;
		}

		double squaredDistance = closest.squaredDistanceTo(self);
		if (squaredDistance <= 1024.0D) {
			ci.cancel();
			return;
		}

		if (squaredDistance >= 16384.0D) {
			self.remove(Entity.RemovalReason.DISCARDED);
			ci.cancel();
		}
	}
}
