package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
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
	private void worstluck$alwaysLeader(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
		ZombieEntity self = (ZombieEntity) (Object) this;
		EntityAttributeInstance reinforcements = self.getAttributeInstance(EntityAttributes.SPAWN_REINFORCEMENTS);
		if (reinforcements != null) {
			reinforcements.setBaseValue(1.0);
			if (reinforcements.getModifier(WORSTLUCK_LEADER_ID) == null) {
				reinforcements.addPersistentModifier(new EntityAttributeModifier(WORSTLUCK_LEADER_ID, 0.75, EntityAttributeModifier.Operation.ADD_VALUE));
			}
		}
		EntityAttributeInstance health = self.getAttributeInstance(EntityAttributes.MAX_HEALTH);
		if (health != null && health.getModifier(WORSTLUCK_LEADER_ID) == null) {
			health.addPersistentModifier(new EntityAttributeModifier(WORSTLUCK_LEADER_ID, 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
			self.setHealth(self.getMaxHealth());
		}
	}

	/** Disable the vanilla probabilistic branch; the return injection below performs one reliable call. */
	@Redirect(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"), require = 1)
	private float worstluck$replaceVanillaReinforcementRoll(Random random) {
		return Float.MAX_VALUE;
	}

	@Inject(method = "damage", at = @At("RETURN"), require = 1)
	private void worstluck$forceReinforcement(ServerWorld world, DamageSource source, float amount,
			CallbackInfoReturnable<Boolean> cir) {
		if (!Boolean.TRUE.equals(cir.getReturnValue())
				|| world.getDifficulty() != Difficulty.HARD
				|| !world.shouldSpawnMonsters()) {
			return;
		}

		ZombieEntity self = (ZombieEntity) (Object) this;
		LivingEntity attacker = self.getTarget();
		if (attacker == null && source.getAttacker() instanceof LivingEntity livingAttacker) {
			attacker = livingAttacker;
		}
		if (attacker == null) {
			return;
		}

		EntityType<? extends ZombieEntity> entityType = self.getType();
		ZombieEntity reinforcement = entityType.create(world, SpawnReason.REINFORCEMENT);
		if (reinforcement == null) {
			return;
		}

		Random random = world.getRandom();
		int baseX = MathHelper.floor(self.getX());
		int baseY = MathHelper.floor(self.getY());
		int baseZ = MathHelper.floor(self.getZ());

		for (int attempt = 0; attempt < 50; attempt++) {
			int rx = baseX + MathHelper.nextInt(random, 7, 40) * MathHelper.nextInt(random, -1, 1);
			int ry = baseY + MathHelper.nextInt(random, 7, 40) * MathHelper.nextInt(random, -1, 1);
			int rz = baseZ + MathHelper.nextInt(random, 7, 40) * MathHelper.nextInt(random, -1, 1);
			BlockPos spawnPos = new BlockPos(rx, ry, rz);

			if (!SpawnRestriction.isSpawnPosAllowed(entityType, world, spawnPos)
					|| !SpawnRestriction.canSpawn(entityType, world, SpawnReason.REINFORCEMENT, spawnPos, random)) {
				continue;
			}

			reinforcement.refreshPositionAndAngles(rx + 0.5D, ry, rz + 0.5D, random.nextFloat() * 360.0F, 0.0F);
			if (world.isPlayerInRange(rx, ry, rz, 7.0D)
					|| !world.doesNotIntersectEntities(reinforcement)
					|| !world.isSpaceEmpty(reinforcement)
					|| world.containsFluid(reinforcement.getBoundingBox())) {
				continue;
			}

			reinforcement.setTarget(attacker);
			reinforcement.initialize(world, world.getLocalDifficulty(spawnPos), SpawnReason.REINFORCEMENT, null);
			world.spawnEntityAndPassengers(reinforcement);
			return;
		}
	}
}
