package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrownedEntity.class)
public class DrownedTridentMixin {
	@Inject(method = "initEquipment", at = @At("TAIL"), require = 0)
	private void worstluck$alwaysTrident(Random random, LocalDifficulty difficulty, CallbackInfo ci) {
		((DrownedEntity) (Object) this).equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
	}
}
