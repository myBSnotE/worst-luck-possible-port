package com.worstluckpossible.mixin.spawns;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Blocks runtime animal/fish spawning while preserving seed-dependent chunk-generation animals. */
@Mixin(SpawnHelper.class)
public class SpawnHelperNoAnimalsMixin {
	@Inject(method = "collectSpawnableGroups", at = @At("RETURN"), cancellable = true)
	private static void worstluck$removeNaturalAnimals(SpawnHelper.Info info, boolean spawnAnimals,
			boolean spawnMonsters, boolean rare, CallbackInfoReturnable<List<SpawnGroup>> cir) {
		List<SpawnGroup> groups = new ArrayList<>(cir.getReturnValue());
		groups.remove(SpawnGroup.CREATURE);
		groups.remove(SpawnGroup.WATER_CREATURE);
		groups.remove(SpawnGroup.WATER_AMBIENT);
		groups.remove(SpawnGroup.AXOLOTLS);
		groups.remove(SpawnGroup.UNDERGROUND_WATER_CREATURE);
		cir.setReturnValue(groups);
	}
}
