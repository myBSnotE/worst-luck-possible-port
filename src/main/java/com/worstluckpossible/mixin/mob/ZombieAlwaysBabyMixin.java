package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public class ZombieAlwaysBabyMixin {
	@Inject(method = "shouldBeBaby", at = @At("HEAD"), cancellable = true, require = 0)
	private void worstluck$alwaysBaby(Random random, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}
}
