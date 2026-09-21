package com.worstluckpossible.mixin.spawns;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Despawns non-persistent hostile mobs beyond 64 blocks from the nearest player. */
@Mixin(MobEntity.class)
public class MobNeverDespawnMixin {
	@Inject(method = "checkDespawn", at = @At("HEAD"), cancellable = true)
	private void worstluck$earlyMonsterDespawn(CallbackInfo ci) {
		MobEntity self = (MobEntity) (Object) this;
		if (self.getType().getSpawnGroup() != SpawnGroup.MONSTER
				|| self.isPersistent() || self.cannotDespawn()) {
			return;
		}
		Entity closest = self.getEntityWorld().getClosestPlayer(self, -1.0);
		if (closest != null && closest.squaredDistanceTo(self) >= 4096.0) {
			self.remove(RemovalReason.DISCARDED);
			ci.cancel();
		}
	}
}
