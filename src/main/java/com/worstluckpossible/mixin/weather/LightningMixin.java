package com.worstluckpossible.mixin.weather;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** Every loaded ticking chunk attempts a lightning strike every thunder tick. */
@Mixin(ServerWorld.class)
public class LightningMixin {
	@ModifyConstant(method = "tickThunder", constant = @Constant(intValue = 100000), require = 0)
	private int worstluck$lightningEveryTick(int original) {
		return 1;
	}
}
