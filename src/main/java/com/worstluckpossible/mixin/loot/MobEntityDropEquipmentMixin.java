package com.worstluckpossible.mixin.loot;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
}
