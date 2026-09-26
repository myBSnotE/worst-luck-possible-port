package com.worstluckpossible.mixin.loot;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.EnchantedCountIncreaseLootFunction;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Keeps Looting useful even though the global minimum-number mixin would
 * otherwise reduce its usual 0..level bonus to zero.
 */
@Mixin(EnchantedCountIncreaseLootFunction.class)
public abstract class EnchantedCountIncreaseLootFunctionMixin {
	@Shadow @Final private RegistryEntry<Enchantment> enchantment;
	@Shadow @Final private int limit;

	@Inject(method = "process", at = @At("HEAD"), cancellable = true)
	private void worstluck$addOnePerLootingLevel(ItemStack stack, LootContext context,
			CallbackInfoReturnable<ItemStack> cir) {
		if (!enchantment.matchesKey(Enchantments.LOOTING)) {
			return;
		}

		Entity attacker = context.get(LootContextParameters.ATTACKING_ENTITY);
		if (!(attacker instanceof LivingEntity living)) {
			cir.setReturnValue(stack);
			return;
		}

		int level = EnchantmentHelper.getEquipmentLevel(enchantment, living);
		if (level > 0) {
			stack.increment(level);
			if (limit > 0) {
				stack.capCount(limit);
			}
		}
		cir.setReturnValue(stack);
	}
}