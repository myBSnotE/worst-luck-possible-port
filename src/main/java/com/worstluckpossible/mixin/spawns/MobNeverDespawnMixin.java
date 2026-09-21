package com.worstluckpossible.mixin.spawns;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Restores the original early hostile despawn beyond 64 blocks. */
@Mixin(MobEntity.class)
public class MobNeverDespawnMixin {
	@Inject(method = "canImmediatelyDespawn", at = @At("HEAD"), cancellable = true, require = 0)
	private void worstluck$earlyMonsterDespawn(double distanceSquared, CallbackInfoReturnable<Boolean> cir) {
		MobEntity self = (MobEntity) (Object) this;
		if (self.getType().getSpawnGroup() == SpawnGroup.MONSTER
				&& distanceSquared >= 4096.0
				&& !self.isPersistent()
				&& !self.cannotDespawn()
				&& !(self.getTarget() instanceof PlayerEntity)) {
			cir.setReturnValue(true);
		}
	}
}
