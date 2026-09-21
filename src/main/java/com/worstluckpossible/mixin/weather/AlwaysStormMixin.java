package com.worstluckpossible.mixin.weather;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Starts an Overworld thunderstorm every ten real-time minutes. */
@Mixin(ServerWorld.class)
public class AlwaysStormMixin {
	@Unique
	private static final long WORSTLUCK_STORM_INTERVAL = 12_000L;
	@Unique
	private static final int WORSTLUCK_STORM_DURATION = 6_000;
	@Unique
	private long worstluck$nextStormTime = Long.MIN_VALUE;

	@Inject(method = "tickWeather", at = @At("TAIL"))
	private void worstluck$scheduleStorm(CallbackInfo ci) {
		ServerWorld self = (ServerWorld) (Object) this;
		if (!self.getRegistryKey().equals(World.OVERWORLD)) {
			return;
		}

		long time = self.getTime();
		if (worstluck$nextStormTime == Long.MIN_VALUE) {
			worstluck$nextStormTime = time + WORSTLUCK_STORM_INTERVAL;
			return;
		}

		if (time >= worstluck$nextStormTime) {
			self.setWeather(0, WORSTLUCK_STORM_DURATION, true, true);
			worstluck$nextStormTime = time + WORSTLUCK_STORM_INTERVAL;
		}
	}
}
