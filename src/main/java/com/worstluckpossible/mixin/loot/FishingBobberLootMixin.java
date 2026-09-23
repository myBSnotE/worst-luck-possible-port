package com.worstluckpossible.mixin.loot;

import com.worstluckpossible.feature.FishingLootMode;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootWorldContext;
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
		if (!FishingLootMode.shouldForceLeatherBoots(lootContext.getWorld().getServer())) {
			return lootTable.generateLoot(lootContext);
		}

		ObjectArrayList<ItemStack> loot = new ObjectArrayList<>();
		loot.add(new ItemStack(Items.LEATHER_BOOTS));
		return loot;
	}
}
