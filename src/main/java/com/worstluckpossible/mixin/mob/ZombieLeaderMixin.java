package com.worstluckpossible.mixin.mob;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
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

	@Unique
	private static final double WORSTLUCK_REINFORCEMENT_RADIUS = 128.0D;

	@Unique
	private static final double WORSTLUCK_REINFORCEMENT_RADIUS_SQUARED =
			WORSTLUCK_REINFORCEMENT_RADIUS * WORSTLUCK_REINFORCEMENT_RADIUS;

	@Unique
	private static final int WORSTLUCK_REINFORCEMENT_MOB_LIMIT = 140;

	@Unique
	private static final int WORSTLUCK_REINFORCEMENT_COUNT_CACHE_TICKS = 20;

	@Unique
	private static final Map<ServerWorld, Map<UUID, WorstluckMobCountState>> WORSTLUCK_MOB_COUNT_CACHE =
			new WeakHashMap<>();

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
	 * Force vanilla's reinforcement roll to pass only while fewer than 140
	 * living mobs exist within 128 blocks of the relevant player. The count is
	 * cached for one second so lightning or other mass-damage events cannot turn
	 * the safety check itself into a lag source.
	 */
	@Redirect(method = "damage", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"), require = 1)
	private float worstluck$reinforcementRollWithMobLimit(
			Random random,
			ServerWorld world,
			DamageSource source,
			float amount
	) {
		ZombieEntity self = (ZombieEntity) (Object) this;
		PlayerEntity player = worstluck$getRelevantPlayer(self, world, source);
		if (player != null && worstluck$countNearbyMobs(world, player) >= WORSTLUCK_REINFORCEMENT_MOB_LIMIT) {
			return Float.MAX_VALUE;
		}
		return 0.0F;
	}

	@Unique
	private static PlayerEntity worstluck$getRelevantPlayer(
			ZombieEntity zombie,
			ServerWorld world,
			DamageSource source
	) {
		Entity attacker = source.getAttacker();
		if (attacker instanceof PlayerEntity player) {
			return player;
		}
		if (zombie.getTarget() instanceof PlayerEntity player) {
			return player;
		}
		return world.getClosestPlayer(zombie, WORSTLUCK_REINFORCEMENT_RADIUS);
	}

	@Unique
	private static int worstluck$countNearbyMobs(ServerWorld world, PlayerEntity player) {
		Map<UUID, WorstluckMobCountState> worldCache = WORSTLUCK_MOB_COUNT_CACHE.computeIfAbsent(
				world,
				ignored -> new HashMap<>()
		);
		long now = world.getTime();
		UUID playerId = player.getUuid();
		WorstluckMobCountState cached = worldCache.get(playerId);
		if (cached != null && now - cached.sampleTick() < WORSTLUCK_REINFORCEMENT_COUNT_CACHE_TICKS) {
			return cached.mobCount();
		}

		int mobCount = world.getEntitiesByClass(
				MobEntity.class,
				player.getBoundingBox().expand(WORSTLUCK_REINFORCEMENT_RADIUS),
				mob -> mob.isAlive()
						&& player.squaredDistanceTo(mob) <= WORSTLUCK_REINFORCEMENT_RADIUS_SQUARED
		).size();
		worldCache.put(playerId, new WorstluckMobCountState(now, mobCount));
		return mobCount;
	}

	@Unique
	private record WorstluckMobCountState(long sampleTick, int mobCount) {
	}
}
