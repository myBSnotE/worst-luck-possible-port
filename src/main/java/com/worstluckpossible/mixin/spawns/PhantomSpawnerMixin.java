package com.worstluckpossible.mixin.spawns;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.rule.GameRules;
import net.minecraft.world.spawner.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Guarantees vanilla-valid phantom attacks after three sleepless days. */
@Mixin(PhantomSpawner.class)
public class PhantomSpawnerMixin {
	private static final int WORSTLUCK_MIN_COOLDOWN = 1200;
	private static final int WORSTLUCK_COOLDOWN_VARIATION = 201;
	private static final int WORSTLUCK_MIN_RESTLESS_TICKS = 72000;
	private static final int WORSTLUCK_MIN_PLAYER_Y = 64;
	private static final int WORSTLUCK_GROUP_SIZE = 4;

	@Shadow
	private int cooldown;

	@Inject(method = "spawn", at = @At("HEAD"), cancellable = true)
	private void worstluck$guaranteedPhantoms(ServerWorld world, boolean spawnMonsters, CallbackInfo ci) {
		ci.cancel();
		if (!spawnMonsters || !world.getGameRules().getValue(GameRules.SPAWN_PHANTOMS)) {
			return;
		}

		if (--cooldown > 0) {
			return;
		}

		Random random = world.getRandom();
		cooldown = WORSTLUCK_MIN_COOLDOWN + random.nextInt(WORSTLUCK_COOLDOWN_VARIATION);

		// Preserve vanilla's night/darkness requirement.
		if (world.getAmbientDarkness() < 5 && world.getDimension().hasSkyLight()) {
			return;
		}

		for (ServerPlayerEntity player : world.getPlayers()) {
			if (player.isSpectator()) {
				continue;
			}

			BlockPos playerPos = player.getBlockPos();
			if (playerPos.getY() < WORSTLUCK_MIN_PLAYER_Y || !world.isSkyVisible(playerPos)) {
				continue;
			}

			ServerStatHandler stats = player.getStatHandler();
			if (stats.getStat(Stats.CUSTOM.getOrCreateStat(Stats.TIME_SINCE_REST))
					< WORSTLUCK_MIN_RESTLESS_TICKS) {
				continue;
			}

			BlockPos spawnPos = playerPos
					.up(20 + random.nextInt(15))
					.east(-9 + random.nextInt(19))
					.south(-9 + random.nextInt(19));
			BlockState blockState = world.getBlockState(spawnPos);
			FluidState fluidState = world.getFluidState(spawnPos);
			if (!SpawnHelper.isClearForSpawn(world, spawnPos, blockState, fluidState, EntityType.PHANTOM)) {
				continue;
			}

			LocalDifficulty difficulty = world.getLocalDifficulty(playerPos);
			EntityData entityData = null;
			for (int index = 0; index < WORSTLUCK_GROUP_SIZE; index++) {
				PhantomEntity phantom = EntityType.PHANTOM.create(world, SpawnReason.NATURAL);
				if (phantom == null) {
					continue;
				}

				phantom.refreshPositionAndAngles(spawnPos, 0.0F, 0.0F);
				entityData = phantom.initialize(world, difficulty, SpawnReason.NATURAL, entityData);
				world.spawnEntityAndPassengers(phantom);
			}
		}
	}
}
