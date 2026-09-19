package com.worstluckpossible.mixin.loot;

import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomChanceLootCondition.class)
public class RandomChanceLootConditionMixin {
	@Inject(method = "test(Lnet/minecraft/loot/context/LootContext;)Z", at = @At("HEAD"), cancellable = true)
	private void worstluck$alwaysFail(LootContext context, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}
}
