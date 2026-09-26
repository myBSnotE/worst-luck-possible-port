package com.worstluckpossible.mixin.loot;

import net.minecraft.entity.EquipmentDropChances;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEntityDropEquipmentMixin {
	@Inject(method = "dropEquipment", at = @At("HEAD"), cancellable = true, require = 0)
	private void worstluck$onlyDropForeignEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
		// Vanilla marks items picked up from the world with an exact/guaranteed drop chance.
		// Keep those ownership-preserving drops, while suppressing naturally generated equipment.
		((MobEntity) (Object) this).dropAllForeignEquipment(world);
		ci.cancel();
	}

	@Redirect(
		method = "tryEquip",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EquipmentDropChances;get(Lnet/minecraft/entity/EquipmentSlot;)F")
	)
	private float worstluck$neverDropNaturalEquipmentWhenReplacing(EquipmentDropChances chances, EquipmentSlot slot) {
		float chance = chances.get(slot);
		// Preserve vanilla's guaranteed return for previously picked-up/foreign items,
		// but make the random 8.5% replacement drop for natural equipment impossible.
		return chance > EquipmentDropChances.UNHARMED_DROP_THRESHOLD ? chance : 0.0F;
	}
}
