package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public final class HorseWorstAttributesMixin {
	private HorseWorstAttributesMixin() {}

	@Mixin(HorseEntity.class)
	public static class HorseInitialization {
		@Inject(method = "initAttributes", at = @At("RETURN"))
		private void worstluck$minimumNaturalHorse(Random random, CallbackInfo ci) {
			AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;
			horse.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(15.0D);
			horse.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.1125D);
			horse.getAttributeInstance(EntityAttributes.JUMP_STRENGTH).setBaseValue(0.4D);
		}
	}

	@Mixin(AbstractDonkeyEntity.class)
	public static class DonkeyInitialization {
		@Inject(method = "initAttributes", at = @At("RETURN"))
		private void worstluck$minimumNaturalDonkeyHealth(Random random, CallbackInfo ci) {
			AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;
			horse.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(15.0D);
		}
	}

	@Mixin(AbstractHorseEntity.class)
	public static class Breeding {
		@Inject(method = "setChildAttributes", at = @At("RETURN"))
		private void worstluck$minimumChildAttributes(PassiveEntity other, AbstractHorseEntity child, CallbackInfo ci) {
			child.getAttributeInstance(EntityAttributes.MAX_HEALTH).setBaseValue(15.0D);
			child.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.1125D);
			child.getAttributeInstance(EntityAttributes.JUMP_STRENGTH).setBaseValue(0.4D);
			child.setHealth(child.getMaxHealth());
		}
	}
}
