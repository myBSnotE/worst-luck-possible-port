package com.worstluckpossible.mixin.loot;

import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(UniformLootNumberProvider.class)
public class UniformLootNumberProviderMixin {
	@Inject(method = "nextFloat", at = @At("HEAD"), cancellable = true, require = 0)
	private void worstluck$alwaysMinFloat(LootContext context, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(((UniformLootNumberProvider) (Object) this).min().nextFloat(context));
	}

	@Inject(method = "nextInt", at = @At("HEAD"), cancellable = true, require = 0)
	private void worstluck$alwaysMinInt(LootContext context, CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(((UniformLootNumberProvider) (Object) this).min().nextInt(context));
	}
}
