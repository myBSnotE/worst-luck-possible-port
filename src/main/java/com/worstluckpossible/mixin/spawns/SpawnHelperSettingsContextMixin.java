package com.worstluckpossible.mixin.spawns;

import com.worstluckpossible.config.WorstLuckConfigManager;
import com.worstluckpossible.feature.SpawnConfigContext;
import java.util.List;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpawnHelper.class)
public abstract class SpawnHelperSettingsContextMixin {
	@Inject(method = "spawn", at = @At("HEAD"))
	private static void worstluck$enterSettings(ServerWorld world, WorldChunk chunk,
			SpawnHelper.Info info, List<SpawnGroup> groups, CallbackInfo ci) {
		SpawnConfigContext.enter(WorstLuckConfigManager.get(world.getServer()));
	}

	@Inject(method = "spawn", at = @At("RETURN"))
	private static void worstluck$exitSettings(ServerWorld world, WorldChunk chunk,
			SpawnHelper.Info info, List<SpawnGroup> groups, CallbackInfo ci) {
		SpawnConfigContext.exit();
	}
}