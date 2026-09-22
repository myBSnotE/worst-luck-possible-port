package com.worstluckpossible.mixin.spawns;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Prioritizes hostile natural spawns near the player while retaining farther fallbacks. */
@Mixin(SpawnHelper.class)
public class SpawnHelperProximityMixin {
	@Unique
	private static final ThreadLocal<Boolean> WORSTLUCK_MONSTER_SPAWN = ThreadLocal.withInitial(() -> false);

	@Inject(
		method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
		at = @At("HEAD")
	)
	private static void worstluck$beginMonsterSpawn(SpawnGroup group, ServerWorld world, Chunk chunk,
			BlockPos pos, SpawnHelper.Checker checker, SpawnHelper.Runner runner, CallbackInfo ci) {
		WORSTLUCK_MONSTER_SPAWN.set(group == SpawnGroup.MONSTER);
	}

	@Inject(
		method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V",
		at = @At("RETURN")
	)
	private static void worstluck$endMonsterSpawn(SpawnGroup group, ServerWorld world, Chunk chunk,
			BlockPos pos, SpawnHelper.Checker checker, SpawnHelper.Runner runner, CallbackInfo ci) {
		WORSTLUCK_MONSTER_SPAWN.remove();
	}

	@ModifyConstant(method = "isAcceptableSpawnPosition", constant = @Constant(doubleValue = 576.0D))
	private static double worstluck$allowSixteenBlockMonsterSpawns(double vanillaMinimumSquared) {
		return WORSTLUCK_MONSTER_SPAWN.get() ? 256.0D : vanillaMinimumSquared;
	}

	@Inject(method = "canSpawn", at = @At("HEAD"), cancellable = true)
	private static void worstluck$monstersNearPlayer(ServerWorld world, SpawnGroup group,
			StructureAccessor structures, ChunkGenerator generator, SpawnSettings.SpawnEntry entry,
			BlockPos.Mutable pos, double squaredDistance, CallbackInfoReturnable<Boolean> cir) {
		if (group != SpawnGroup.MONSTER) {
			return;
		}
		if (squaredDistance <= 256.0D || squaredDistance > 1024.0D) {
			cir.setReturnValue(false);
			return;
		}
		PlayerEntity player = world.getClosestPlayer(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, -1.0, false);
		if (player == null || Math.abs(player.getBlockY() - pos.getY()) > 16) {
			cir.setReturnValue(false);
			return;
		}

		// The 16-20 block band is always attempted. Farther valid positions remain
		// available as progressively less likely fallbacks when the near band is lit
		// or otherwise invalid.
		if (squaredDistance > 400.0D) {
			int divisor = squaredDistance <= 576.0D ? 4 : squaredDistance <= 784.0D ? 8 : 16;
			if (world.getRandom().nextInt(divisor) != 0) {
				cir.setReturnValue(false);
			}
		}
	}
}
