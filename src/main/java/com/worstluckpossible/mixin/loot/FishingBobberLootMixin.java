package com.worstluckpossible.mixin.loot;

import com.worstluckpossible.config.WorstLuckConfig;
import com.worstluckpossible.config.WorstLuckConfigManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberLootMixin {
	@Redirect(
			method = "use",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/loot/LootTable;generateLoot(Lnet/minecraft/loot/context/LootWorldContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;"
			)
	)
	private ObjectArrayList<ItemStack> worstluck$forceLeatherBoots(
			LootTable lootTable,
			LootWorldContext lootContext
	) {
		if (WorstLuckConfigManager.get(lootContext.getWorld().getServer()).fishingMode
				!= WorstLuckConfig.FishingMode.BAD) {
			return lootTable.generateLoot(lootContext);
		}

		ItemStack boots = new ItemStack(Items.LEATHER_BOOTS);
		boots.setDamage(boots.getMaxDamage());

		ObjectArrayList<ItemStack> loot = new ObjectArrayList<>();
		loot.add(boots);
		return loot;
	}

	@Redirect(
			method = "tickFishingLogic",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/math/MathHelper;nextInt(Lnet/minecraft/util/math/random/Random;II)I"
			)
	)
	private int worstluck$maximizeRandomWait(Random random, int min, int max) {
		// Only replace the initial 100..600-tick waiting roll. Rain, sky access,
		// Lure and all later fishing phases retain their vanilla influence.
		FishingBobberEntity self = (FishingBobberEntity) (Object) this;
		WorstLuckConfig.FishingMode mode =
				WorstLuckConfigManager.get(self.getEntityWorld().getServer()).fishingMode;
		return mode != WorstLuckConfig.FishingMode.VANILLA && min == 100 && max == 600
				? max : MathHelper.nextInt(random, min, max);
	}
}
