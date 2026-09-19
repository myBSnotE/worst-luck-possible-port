package com.worstluckpossible.mixin.spawns;

import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Mobs never despawn on their own, so crowds keep piling up around the player. */
@Mixin(MobEntity.class)
public class MobNeverDespawnMixin {
	@Inject(method = "canImmediatelyDespawn", at = @At("HEAD"), cancellable = true, require = 0)
	private void worstluck$neverDespawn(double distanceSquared, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}
}
