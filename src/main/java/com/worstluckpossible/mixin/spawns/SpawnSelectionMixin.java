package com.worstluckpossible.mixin.spawns;

import java.util.Optional;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Ports drowned preference, piglin avoidance, and strider suppression safely. */
@Mixin(SpawnHelper.class)
public class SpawnSelectionMixin {
	@Inject(method = "pickRandomSpawnEntry", at = @At("RETURN"), cancellable = true)
	private static void worstluck$preferWorstSpawn(ServerWorld world, StructureAccessor structures,
			ChunkGenerator generator, SpawnGroup group, Random random, BlockPos pos,
			CallbackInfoReturnable<Optional<SpawnSettings.SpawnEntry>> cir) {
		Optional<SpawnSettings.SpawnEntry> selected = cir.getReturnValue();
		if (selected.isEmpty()) {
			return;
		}
		SpawnSettings.SpawnEntry entry = selected.get();
		if (entry.type() == EntityType.STRIDER) {
			cir.setReturnValue(Optional.empty());
			return;
		}
		if (group != SpawnGroup.MONSTER) {
			return;
		}
		boolean wet = world.getFluidState(pos).isIn(FluidTags.WATER)
				|| world.getFluidState(pos.down()).isIn(FluidTags.WATER);
		EntityType<?> replacement = wet ? EntityType.DROWNED
				: entry.type() == EntityType.ZOMBIFIED_PIGLIN ? EntityType.GHAST : null;
		if (replacement != null) {
			cir.setReturnValue(Optional.of(new SpawnSettings.SpawnEntry(
					replacement, entry.maxGroupSize(), entry.maxGroupSize())));
		}
	}
}
