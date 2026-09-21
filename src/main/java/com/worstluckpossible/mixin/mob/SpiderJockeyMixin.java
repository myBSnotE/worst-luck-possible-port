package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpiderEntity.class)
public class SpiderJockeyMixin {
	@Inject(method = "initialize", at = @At("RETURN"), require = 0)
	private void worstluck$alwaysJockey(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
		SpiderEntity self = (SpiderEntity) (Object) this;
		if (world.getDifficulty() == Difficulty.HARD) {
			self.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, -1, 0, false, false));
		}
		if (!(world.toServerWorld() instanceof ServerWorld serverWorld) || self.hasPassengers()) {
			return;
		}
		SkeletonEntity skeleton = EntityType.SKELETON.create(serverWorld, SpawnReason.JOCKEY);
		if (skeleton == null) {
			return;
		}
		skeleton.refreshPositionAndAngles(self.getX(), self.getY(), self.getZ(), self.getYaw(), 0.0F);
		skeleton.initialize(world, difficulty, SpawnReason.JOCKEY, null);
		serverWorld.spawnEntity(skeleton);
		skeleton.startRiding(self);
	}
}
