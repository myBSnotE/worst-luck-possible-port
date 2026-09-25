package com.worstluckpossible.mixin.trade;

import com.worstluckpossible.feature.WorstTradeOffers;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TradeOffers.class)
public abstract class TradeOffersMixin {
	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void worstluck$installWorstTrades(CallbackInfo ci) {
		WorstTradeOffers.install();
	}
}
