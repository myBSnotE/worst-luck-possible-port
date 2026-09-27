package com.worstluckpossible.mixin.spawns;

import com.worstluckpossible.config.WorstLuckConfig;
import com.worstluckpossible.feature.SpawnConfigContext;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/** Original values: six waves, eight preliminary attempts, four-block jitter. */
@Mixin(SpawnHelper.class)
public class SpawnHelperIntensityMixin {
	@ModifyConstant(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V", constant = @Constant(intValue = 3), require = 0)
	private static int worstluck$moreWaves(int original) {
		WorstLuckConfig config = SpawnConfigContext.current();
		return config != null && config.hostilePackMode == WorstLuckConfig.HostilePackMode.MAXIMUM
				? 6 : original;
	}

	@ModifyConstant(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V", constant = @Constant(floatValue = 4.0F), require = 0)
	private static float worstluck$moreGroupAttempts(float original) {
		WorstLuckConfig config = SpawnConfigContext.current();
		return config != null && config.hostilePackMode == WorstLuckConfig.HostilePackMode.MAXIMUM
				? 8.0F : original;
	}

	@ModifyConstant(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V", constant = @Constant(intValue = 6), require = 0)
	private static int worstluck$tighterJitter(int original) {
		WorstLuckConfig config = SpawnConfigContext.current();
		return config != null && config.hostilePackMode == WorstLuckConfig.HostilePackMode.MAXIMUM
				? 4 : original;
	}
}
