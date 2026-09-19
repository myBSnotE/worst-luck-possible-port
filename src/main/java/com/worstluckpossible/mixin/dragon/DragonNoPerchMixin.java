package com.worstluckpossible.mixin.dragon;

import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** The ender dragon never lands or perches: landing and sitting phases become circling. */
@Mixin(PhaseManager.class)
public class DragonNoPerchMixin {
	@ModifyVariable(method = "setPhase", at = @At("HEAD"), argsOnly = true, require = 0)
	private PhaseType<?> worstluck$noPerch(PhaseType<?> type) {
		if (type == PhaseType.LANDING
				|| type == PhaseType.LANDING_APPROACH
				|| type == PhaseType.SITTING_SCANNING
				|| type == PhaseType.SITTING_ATTACKING
				|| type == PhaseType.SITTING_FLAMING) {
			return PhaseType.HOLDING_PATTERN;
		}
		return type;
	}
}
