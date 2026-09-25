package com.worstluckpossible.mixin.weather;

import com.worstluckpossible.feature.LightningRateMode;
import com.worstluckpossible.feature.MobPressureCache;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
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

	@Shadow
	protected abstract BlockPos getLightningPos(BlockPos pos);

	@Inject(method = "tickThunder", at = @At("HEAD"), cancellable = true)
	private void worstluck$controlLightningAndHorseTraps(WorldChunk chunk, CallbackInfo ci) {
		ci.cancel();

		ServerWorld world = (ServerWorld) (Object) this;
		int interval = LightningRateMode.isReduced(world.getServer()) ? 20 : 1;
		if (!world.isRaining() || !world.isThundering() || world.getRandom().nextInt(interval) != 0) {
			return;
		}

		ChunkPos tickingChunk = chunk.getPos();
		BlockPos randomPos = new BlockPos(
				tickingChunk.getStartX() + world.getRandom().nextInt(16),
				0,
				tickingChunk.getStartZ() + world.getRandom().nextInt(16)
		);
		BlockPos lightningPos = this.getLightningPos(randomPos);
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
