package com.worstluckpossible.mixin.block;

import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** Makes the vanilla 12% anvil-damage roll succeed after every non-creative use. */
@Mixin(AnvilScreenHandler.class)
public class AnvilAlwaysDamagesMixin {
	@ModifyConstant(method = {"onTakeOutput", "method_24922"}, constant = @Constant(floatValue = 0.12F), require = 1)
	private static float worstluck$alwaysDamageAnvil(float original) {
		return 1.0F;
	}
}
