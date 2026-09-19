package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public class ZombieLeaderMixin {
	private static final Identifier WORSTLUCK_LEADER_ID = Identifier.of("worst-luck-possible", "leader_bonus");

	@Inject(method = "initialize", at = @At("RETURN"), require = 0)
	private void worstluck$alwaysLeader(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
		ZombieEntity self = (ZombieEntity) (Object) this;
		EntityAttributeInstance health = self.getAttributeInstance(EntityAttributes.MAX_HEALTH);
		if (health != null && health.getModifier(WORSTLUCK_LEADER_ID) == null) {
			health.addPersistentModifier(new EntityAttributeModifier(WORSTLUCK_LEADER_ID, 10.0, EntityAttributeModifier.Operation.ADD_VALUE));
			self.setHealth(self.getMaxHealth());
		}
	}
}
