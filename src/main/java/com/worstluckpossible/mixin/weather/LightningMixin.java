package com.worstluckpossible.mixin.weather;

import com.worstluckpossible.config.WorstLuckConfig;
import com.worstluckpossible.config.WorstLuckConfigManager;
import com.worstluckpossible.feature.MobPressureCache;
import java.util.List;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.rule.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Controls lightning frequency and makes skeleton-horse traps relevant to nearby players. */
@Mixin(ServerWorld.class)
public abstract class LightningMixin {
	private static final double WORSTLUCK_TRAP_TRIGGER_RADIUS = 10.0D;
	private static final double WORSTLUCK_TRAP_TRIGGER_RADIUS_SQUARED =
			WORSTLUCK_TRAP_TRIGGER_RADIUS * WORSTLUCK_TRAP_TRIGGER_RADIUS;
	// Trap riders never naturally despawn, so stop creating new groups before they become a lag machine.
	private static final int WORSTLUCK_MAX_PERSISTENT_MOBS_NEAR_PLAYER = 96;
	private static final int WORSTLUCK_FLAMMABLE_SEARCH_ATTEMPTS = 64;

	@Shadow
	protected abstract BlockPos getLightningPos(BlockPos pos);

	@Inject(method = "tickThunder", at = @At("HEAD"), cancellable = true)
	private void worstluck$controlLightningAndHorseTraps(WorldChunk chunk, CallbackInfo ci) {
		ServerWorld world = (ServerWorld) (Object) this;
		WorstLuckConfig config = WorstLuckConfigManager.get(world.getServer());
		if (config.lightningTargets == WorstLuckConfig.LightningTargets.VANILLA) {
			return;
		}
		ci.cancel();

		if (!world.isRaining()
				|| !world.isThundering()
				|| world.getRandom().nextInt(100) >= config.lightningFrequencyPercent) {
			return;
		}

		ChunkPos tickingChunk = chunk.getPos();
		BlockPos lightningPos = worstluck$chooseLightningPos(world, tickingChunk, config.lightningTargets);
		if (lightningPos == null) {
			return;
		}
		if (!world.hasRain(lightningPos)) {
			return;
		}

		ServerPlayerEntity nearbyPlayer = worstluck$getNearbyPlayerInStrikeChunk(world, lightningPos);
		boolean spawnTrap = world.getGameRules().getValue(GameRules.DO_MOB_SPAWNING)
				&& !world.getBlockState(lightningPos.down()).isIn(BlockTags.LIGHTNING_RODS)
				&& nearbyPlayer != null
				&& MobPressureCache.get(world, nearbyPlayer).persistentMobs()
						< WORSTLUCK_MAX_PERSISTENT_MOBS_NEAR_PLAYER;

		if (spawnTrap) {
			SkeletonHorseEntity horse = EntityType.SKELETON_HORSE.create(world, SpawnReason.EVENT);
			if (horse != null) {
				horse.setTrapped(true);
				horse.setBreedingAge(0);
				horse.setPosition(lightningPos.getX(), lightningPos.getY(), lightningPos.getZ());
				world.spawnEntity(horse);
			}
		}

		LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world, SpawnReason.EVENT);
		if (lightning != null) {
			lightning.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(lightningPos));
			lightning.setCosmetic(spawnTrap);
			world.spawnEntity(lightning);
		}
	}

	private BlockPos worstluck$chooseLightningPos(ServerWorld world, ChunkPos chunk,
			WorstLuckConfig.LightningTargets mode) {
		if (mode == WorstLuckConfig.LightningTargets.LOADED_AREA) {
			BlockPos randomPos = new BlockPos(
					chunk.getStartX() + world.getRandom().nextInt(16),
					0,
					chunk.getStartZ() + world.getRandom().nextInt(16));
			return this.getLightningPos(randomPos);
		}

		Box chunkBox = new Box(chunk.getStartX(), world.getBottomY(), chunk.getStartZ(),
				chunk.getEndX() + 1, world.getTopYInclusive() + 1, chunk.getEndZ() + 1);
		List<LivingEntity> candidates = world.getEntitiesByClass(LivingEntity.class, chunkBox,
				entity -> entity.isAlive()
						&& !entity.isSpectator()
						&& (entity instanceof ServerPlayerEntity || entity instanceof PassiveEntity)
						&& world.hasRain(entity.getBlockPos()));
		List<LivingEntity> players = candidates.stream()
				.filter(ServerPlayerEntity.class::isInstance)
				.toList();
		if (!players.isEmpty()) {
			return players.get(world.getRandom().nextInt(players.size())).getBlockPos();
		}
		if (!candidates.isEmpty()) {
			return candidates.get(world.getRandom().nextInt(candidates.size())).getBlockPos();
		}

		if (mode != WorstLuckConfig.LightningTargets.PLAYERS_PASSIVES_AND_FLAMMABLES) {
			return null;
		}

		for (int attempt = 0; attempt < WORSTLUCK_FLAMMABLE_SEARCH_ATTEMPTS; attempt++) {
			BlockPos surface = this.getLightningPos(new BlockPos(
					chunk.getStartX() + world.getRandom().nextInt(16),
					0,
					chunk.getStartZ() + world.getRandom().nextInt(16)));
			if (world.hasRain(surface) && world.getBlockState(surface.down()).isBurnable()) {
				return surface;
			}
		}
		return null;
	}

	private static ServerPlayerEntity worstluck$getNearbyPlayerInStrikeChunk(ServerWorld world, BlockPos lightningPos) {
		ChunkPos strikeChunk = new ChunkPos(lightningPos);
		Vec3d strikeCenter = Vec3d.ofBottomCenter(lightningPos);
		for (ServerPlayerEntity player : world.getPlayers()) {
			if (player.isAlive()
					&& !player.isSpectator()
					&& player.getChunkPos().equals(strikeChunk)
					&& player.squaredDistanceTo(strikeCenter) <= WORSTLUCK_TRAP_TRIGGER_RADIUS_SQUARED) {
				return player;
			}
		}
		return null;
	}
}
