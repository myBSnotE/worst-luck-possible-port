package com.worstluckpossible.mixin.loot;

import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Forces binomial Fortune-style loot bonuses to their minimum value. */
@Mixin(targets = "net.minecraft.loot.function.ApplyBonusLootFunction$BinomialWithBonusCount")
public class BinomialWithBonusCountMixin {
	@Inject(method = "getValue", at = @At("HEAD"), cancellable = true)
	private void worstluck$noBonusDrops(Random random, int initialCount, int enchantmentLevel,
			CallbackInfoReturnable<Integer> cir) {
		cir.setReturnValue(initialCount);
	}
}
