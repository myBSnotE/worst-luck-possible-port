package com.worstluckpossible.mixin.mob;

import com.worstluckpossible.feature.MobPressureCache;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ZombieEntity.class)
public class ZombieLeaderMixin {
	private static final Identifier WORSTLUCK_LEADER_ID = Identifier.of("worst-luck-possible", "leader_bonus");
	@Unique private PlayerEntity worstluck$reinforcementTarget;
	@Unique private int worstluck$reinforcementAxis;

	@Inject(method = "initialize", at = @At("RETURN"), require = 1)
	private void worstluck$alwaysLeader(ServerWorldAccess world, LocalDifficulty difficulty,
			SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
		ZombieEntity self = (ZombieEntity) (Object) this;
		EntityAttributeInstance reinforcements = self.getAttributeInstance(EntityAttributes.SPAWN_REINFORCEMENTS);
		if (reinforcements != null) {
			reinforcements.setBaseValue(1.0);
			if (reinforcements.getModifier(WORSTLUCK_LEADER_ID) == null) {
				reinforcements.addPersistentModifier(new EntityAttributeModifier(
						WORSTLUCK_LEADER_ID, 0.75, EntityAttributeModifier.Operation.ADD_VALUE));
			}
		}
		EntityAttributeInstance health = self.getAttributeInstance(EntityAttributes.MAX_HEALTH);
		if (health != null && health.getModifier(WORSTLUCK_LEADER_ID) == null) {
			health.addPersistentModifier(new EntityAttributeModifier(
					WORSTLUCK_LEADER_ID, 0.5, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
			self.setHealth(self.getMaxHealth());
		}
		if (world.getDifficulty() == Difficulty.HARD) self.setCanBreakDoors(true);
	}

	@Inject(method = "damage", at = @At("HEAD"))
	private void worstluck$prepareReinforcementBias(ServerWorld world, DamageSource source, float amount,
			CallbackInfoReturnable<Boolean> cir) {
		worstluck$reinforcementTarget = worstluck$getRelevantPlayer((ZombieEntity) (Object) this, world, source);
		worstluck$reinforcementAxis = 0;
	}

	@Inject(method = "damage", at = @At("RETURN"))
	private void worstluck$clearReinforcementBias(ServerWorld world, DamageSource source, float amount,
			CallbackInfoReturnable<Boolean> cir) {
		worstluck$reinforcementTarget = null;
	}

	@Redirect(method = "damage", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"), require = 1)
	private float worstluck$reinforcementRollWithMobLimit(Random random, ServerWorld world,
			DamageSource source, float amount) {
		if (worstluck$reinforcementTarget != null
				&& MobPressureCache.get(world, worstluck$reinforcementTarget).totalMobs() >= 140) {
			return Float.MAX_VALUE;
		}
		return 0.0F;
	}

	@Redirect(method = "damage", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/math/MathHelper;nextInt(Lnet/minecraft/util/math/random/Random;II)I"))
	private int worstluck$biasReinforcementCoordinates(Random random, int min, int max,
			ServerWorld world, DamageSource source, float amount) {
		if (min == 7 && max == 40) {
			return 8 + random.nextInt(5);
		}
		if (min != -1 || max != 1 || worstluck$reinforcementTarget == null) {
			return MathHelper.nextInt(random, min, max);
		}
		ZombieEntity self = (ZombieEntity) (Object) this;
		int axis = worstluck$reinforcementAxis++ % 3;
		if (axis == 1) return 0;
		double delta = axis == 0
				? worstluck$reinforcementTarget.getX() - self.getX()
				: worstluck$reinforcementTarget.getZ() - self.getZ();
		int direction = delta == 0.0D ? (random.nextBoolean() ? 1 : -1) : (delta > 0.0D ? 1 : -1);
		return Math.abs(delta) < 10.0D ? -direction : direction;
	}

	@Unique
	private static PlayerEntity worstluck$getRelevantPlayer(ZombieEntity zombie, ServerWorld world, DamageSource source) {
		Entity attacker = source.getAttacker();
		if (attacker instanceof PlayerEntity player) return player;
		if (zombie.getTarget() instanceof PlayerEntity player) return player;
		return world.getClosestPlayer(zombie, 128.0D);
	}
}
