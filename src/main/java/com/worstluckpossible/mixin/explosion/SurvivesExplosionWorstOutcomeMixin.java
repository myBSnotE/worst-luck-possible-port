package com.worstluckpossible.mixin.explosion;

import com.worstluckpossible.config.WorstLuckConfig;
import com.worstluckpossible.config.WorstLuckConfigManager;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Fails survives_explosion whenever loss is an available vanilla random result. */
@Mixin(SurvivesExplosionLootCondition.class)
public abstract class SurvivesExplosionWorstOutcomeMixin {
	@Redirect(
			method = "test(Lnet/minecraft/loot/context/LootContext;)Z",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"
			)
	)
	private float worstluck$failExplosionSurvival(Random random, LootContext context) {
		return WorstLuckConfigManager.get(context.getWorld().getServer()).explosionMode
				== WorstLuckConfig.ExplosionMode.MAXIMUM
				? Math.nextDown(1.0F)
				: random.nextFloat();
	}
}