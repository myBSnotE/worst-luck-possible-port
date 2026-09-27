package com.worstluckpossible.mixin.spawns;

import com.worstluckpossible.config.WorstLuckConfig;
import com.worstluckpossible.config.WorstLuckConfigManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Blocks runtime animal/fish spawning while preserving seed-dependent chunk-generation animals. */
@Mixin(SpawnHelper.class)
public class SpawnHelperNoAnimalsMixin {
	@ModifyVariable(method = "spawn", at = @At("HEAD"), argsOnly = true, index = 3)
	private static List<SpawnGroup> worstluck$removeNaturalAnimals(List<SpawnGroup> original,
			ServerWorld world, WorldChunk chunk, SpawnHelper.Info info) {
		if (WorstLuckConfigManager.get(world.getServer()).passiveSpawnMode
				== WorstLuckConfig.PassiveSpawnMode.VANILLA) {
			return original;
		}
		List<SpawnGroup> groups = new ArrayList<>(original);
		groups.remove(SpawnGroup.CREATURE);
		groups.remove(SpawnGroup.WATER_CREATURE);
		groups.remove(SpawnGroup.WATER_AMBIENT);
		groups.remove(SpawnGroup.AXOLOTLS);
		groups.remove(SpawnGroup.UNDERGROUND_WATER_CREATURE);
		return groups;
	}
}
