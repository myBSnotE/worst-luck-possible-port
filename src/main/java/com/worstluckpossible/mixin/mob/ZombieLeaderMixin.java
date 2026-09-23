package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public class ZombieLeaderMixin {
	private static final Identifier WORSTLUCK_LEADER_ID = Identifier.of("worst-luck-possible", "leader_bonus");

	@Inject(method = "initialize", at = @At("RETURN"), require = 1)
	private void worstluck$alwaysLeader(ServerWorldAccess world, LocalDifficulty difficulty,
			SpawnReason spawnReason, EntityData entityData,
			CallbackInfoReturnable<EntityData> cir) {
		ZombieEntity self = (ZombieEntity) (Object) this;
		EntityAttributeInstance reinforcements = self.getAttributeInstance(EntityAttributes.SPAWN_REINFORCEMENTS);
		if (reinforcements != null) {
			reinforcements.setBaseValue(1.0);
			if (reinforcements.getModifier(WORSTLUCK_LEADER_ID) == null) {
				reinforcements.addPersistentModifier(new EntityAttributeModifier(
						WORSTLUCK_LEADER_ID,
						0.75,
						EntityAttributeModifier.Operation.ADD_VALUE));
			}
		}

		EntityAttributeInstance health = self.getAttributeInstance(EntityAttributes.MAX_HEALTH);
		if (health != null && health.getModifier(WORSTLUCK_LEADER_ID) == null) {
			health.addPersistentModifier(new EntityAttributeModifier(
					WORSTLUCK_LEADER_ID,
					0.5,
					EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
			self.setHealth(self.getMaxHealth());
		}
	}

	/**
	 * Match the original mod: only force the random reinforcement gate to pass.
	 * Vanilla remains responsible for finding a legal position, creating the
	 * reinforcement, assigning its target and applying caller/callee charges.
	 */
	@Redirect(method = "damage", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"), require = 1)
	private float worstluck$alwaysPassReinforcementRoll(Random random) {
		return 0.0F;
	}
}
