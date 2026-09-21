package com.worstluckpossible.mixin.weather;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Matches the original: clear rain/thunder timers are capped at ten minutes. */
@Mixin(ServerWorld.class)
public class AlwaysStormMixin {
	@Shadow @Final private ServerWorldProperties worldProperties;

	@Inject(method = "tickWeather", at = @At("HEAD"))
	private void worstluck$capClearWeatherTimers(CallbackInfo ci) {
		if (!worldProperties.isThundering() && worldProperties.getThunderTime() > 12_000) {
			worldProperties.setThunderTime(12_000);
		}
		if (!worldProperties.isRaining() && worldProperties.getRainTime() > 12_000) {
			worldProperties.setRainTime(12_000);
		}
	}
}
