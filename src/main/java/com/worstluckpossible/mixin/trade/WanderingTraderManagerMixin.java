package com.worstluckpossible.mixin.trade;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.WanderingTraderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Disables natural wandering-trader and trader-llama spawning. */
@Mixin(WanderingTraderManager.class)
public abstract class WanderingTraderManagerMixin {
	@Inject(method = "spawn", at = @At("HEAD"), cancellable = true)
	private void worstluck$disableWanderingTraderSpawn(ServerWorld world, boolean spawnMonsters, CallbackInfo ci) {
		ci.cancel();
	}
}
