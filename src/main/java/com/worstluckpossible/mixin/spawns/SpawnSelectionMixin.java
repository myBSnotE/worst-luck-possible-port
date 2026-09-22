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

/** Ports drowned preference without invalidating vanilla spawn-pool checks. */
@Mixin(SpawnHelper.class)
public class SpawnSelectionMixin {
	@Inject(method = "pickRandomSpawnEntry", at = @At("RETURN"), cancellable = true)
	private static void worstluck$preferDrowned(ServerWorld world, StructureAccessor structures,
			ChunkGenerator generator, SpawnGroup group, Random random, BlockPos pos,
			CallbackInfoReturnable<Optional<SpawnSettings.SpawnEntry>> cir) {
		Optional<SpawnSettings.SpawnEntry> selected = cir.getReturnValue();
		if (selected.isEmpty() || group != SpawnGroup.MONSTER) {
			return;
		}

		SpawnSettings.SpawnEntry entry = selected.get();
		boolean wet = world.getFluidState(pos).isIn(FluidTags.WATER)
				|| world.getFluidState(pos.down()).isIn(FluidTags.WATER);
		if (wet && entry.type() != EntityType.DROWNED) {
			cir.setReturnValue(Optional.of(new SpawnSettings.SpawnEntry(
					EntityType.DROWNED, entry.maxGroupSize(), entry.maxGroupSize())));
		}
	}

	/**
	 * A synthetic drowned entry is deliberately not present in every biome's
	 * spawn pool. Permit it only at valid water positions; all normal drowned
	 * spawn restrictions are still checked immediately afterwards.
	 */
	@Inject(method = "containsSpawnEntry", at = @At("RETURN"), cancellable = true)
	private static void worstluck$allowSyntheticDrowned(ServerWorld world,
			StructureAccessor structures, ChunkGenerator generator, SpawnGroup group,
			SpawnSettings.SpawnEntry entry, BlockPos pos,
			CallbackInfoReturnable<Boolean> cir) {
		if (Boolean.TRUE.equals(cir.getReturnValue())
				|| group != SpawnGroup.MONSTER
				|| entry.type() != EntityType.DROWNED) {
			return;
		}

		boolean wet = world.getFluidState(pos).isIn(FluidTags.WATER)
				|| world.getFluidState(pos.down()).isIn(FluidTags.WATER);
		if (wet) {
			cir.setReturnValue(true);
		}
	}
}
