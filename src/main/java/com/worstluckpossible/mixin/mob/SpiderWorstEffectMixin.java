package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.jspecify.annotations.Nullable;

/** Forces the most useful jockey mobility roll: permanent Speed I on Hard. */
@Mixin(SpiderEntity.class)
public class SpiderWorstEffectMixin {
	@Inject(method = "initialize", at = @At("RETURN"))
	private void worstluck$forceSpeedEffect(ServerWorldAccess world, LocalDifficulty difficulty,
			SpawnReason spawnReason, @Nullable EntityData entityData,
			CallbackInfoReturnable<EntityData> cir) {
		if (world.getDifficulty() == Difficulty.HARD) {
			SpiderEntity spider = (SpiderEntity) (Object) this;
			spider.removeStatusEffect(StatusEffects.STRENGTH);
			spider.removeStatusEffect(StatusEffects.REGENERATION);
			spider.removeStatusEffect(StatusEffects.INVISIBILITY);
			spider.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, -1, 0));
		}
	}
}
