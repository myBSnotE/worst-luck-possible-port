package com.worstluckpossible.mixin.weather;

import com.worstluckpossible.feature.LightningRateMode;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** Controls the per-ticking-chunk lightning attempt interval. */
@Mixin(ServerWorld.class)
public class LightningMixin {
	@ModifyConstant(method = "tickThunder", constant = @Constant(intValue = 100000), require = 0)
	private int worstluck$lightningAttemptInterval(int original) {
		ServerWorld world = (ServerWorld) (Object) this;
		return LightningRateMode.isReduced(world.getServer()) ? 20 : 1;
	}
}
