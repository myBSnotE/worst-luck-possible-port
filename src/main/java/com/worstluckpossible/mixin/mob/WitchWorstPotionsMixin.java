package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.mob.WitchEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** Removes favorable random failures from witch self-preservation and attack potion choices. */
@Mixin(WitchEntity.class)
public class WitchWorstPotionsMixin {
	@ModifyConstant(method = "tickMovement", constant = @Constant(floatValue = 0.15F), require = 2)
	private float worstluck$alwaysDrinkWaterOrFireProtection(float original) {
		return 1.0F;
	}

	@ModifyConstant(method = "tickMovement", constant = @Constant(floatValue = 0.05F))
	private float worstluck$alwaysDrinkHealing(float original) {
		return 1.0F;
	}

	@ModifyConstant(method = "tickMovement", constant = @Constant(floatValue = 0.5F))
	private float worstluck$alwaysDrinkSwiftness(float original) {
		return 1.0F;
	}

	@ModifyConstant(method = "shootAt", constant = @Constant(floatValue = 0.25F))
	private float worstluck$preferHarmingOverWeakness(float original) {
		return 0.0F;
	}
}
