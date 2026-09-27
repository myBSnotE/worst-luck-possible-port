package com.worstluckpossible.mixin.spawns;

import com.worstluckpossible.config.WorstLuckConfig;
import com.worstluckpossible.feature.SpawnConfigContext;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/** Selects the configured maximum pack size; coordinate jitter rolls stay untouched. */
@Mixin(SpawnHelper.class)
public class SpawnHelperMaxPackMixin {
	@Redirect(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I", ordinal = 4))
	private static int worstluck$maxPackSize(Random random, int bound) {
		WorstLuckConfig config = SpawnConfigContext.current();
		if (config == null || config.hostilePackMode == WorstLuckConfig.HostilePackMode.VANILLA) {
			return random.nextInt(bound);
		}
		return bound <= 1 ? 0 : bound - 1;
	}
}
