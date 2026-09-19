package com.worstluckpossible.mixin.weather;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class AlwaysStormMixin {
	@Inject(method = "tickWeather", at = @At("TAIL"), require = 0)
	private void worstluck$forceStorm(CallbackInfo ci) {
		ServerWorld self = (ServerWorld) (Object) this;
		if (!self.isRaining() || !self.isThundering()) {
			self.setWeather(0, 24000, true, true);
		}
	}
}
