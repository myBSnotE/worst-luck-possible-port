package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.mob.ZombieVillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Curing always starts at 6000 ticks and never receives random bed/iron-bar acceleration. */
@Mixin(ZombieVillagerEntity.class)
public class ZombieVillagerWorstCureMixin {
	@ModifyArg(method = "interactMob", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/entity/mob/ZombieVillagerEntity;setConverting(Ljava/util/UUID;I)V"), index = 1)
	private int worstluck$maximumInitialCureTime(int delay) {
		return 6000;
	}

	@Inject(method = "getConversionRate", at = @At("HEAD"), cancellable = true)
	private void worstluck$disableRandomAcceleration(CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(1);
	}
}
