package com.worstluckpossible.mixin.spawns;

import java.util.Optional;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Ports the original drowned/ghast preferences and strider suppression. */
@Mixin(SpawnHelper.class)
public class SpawnSelectionMixin {
	@Shadow
	private static Pool<SpawnSettings.SpawnEntry> getSpawnEntries(ServerWorld world,
			StructureAccessor structures, ChunkGenerator generator, SpawnGroup group,
			BlockPos pos, RegistryEntry<Biome> biome) {
		throw new AssertionError();
	}

	@Inject(method = "pickRandomSpawnEntry", at = @At("RETURN"), cancellable = true)
	private static void worstluck$preferWorstSpawn(ServerWorld world, StructureAccessor structures,
			ChunkGenerator generator, SpawnGroup group, Random random, BlockPos pos,
			CallbackInfoReturnable<Optional<SpawnSettings.SpawnEntry>> cir) {
		Optional<SpawnSettings.SpawnEntry> selected = cir.getReturnValue();
		if (selected.isPresent() && selected.get().type() == EntityType.STRIDER) {
			cir.setReturnValue(Optional.empty());
			return;
		}
		if (group != SpawnGroup.MONSTER) {
			return;
		}
		Pool<SpawnSettings.SpawnEntry> entries = getSpawnEntries(world, structures, generator, group, pos, world.getBiome(pos));
		boolean wet = !world.getFluidState(pos).isEmpty() || !world.getFluidState(pos.down()).isEmpty();
		if (wet && choose(entries, EntityType.DROWNED, cir)) {
			return;
		}
		if (world.getRegistryKey().equals(World.NETHER)) {
			choose(entries, EntityType.GHAST, cir);
		}
	}

	private static boolean choose(Pool<SpawnSettings.SpawnEntry> entries, EntityType<?> type,
			CallbackInfoReturnable<Optional<SpawnSettings.SpawnEntry>> cir) {
		for (Weighted<SpawnSettings.SpawnEntry> weighted : entries.getEntries()) {
			if (weighted.value().type() == type) {
				SpawnSettings.SpawnEntry entry = weighted.value();
				cir.setReturnValue(Optional.of(new SpawnSettings.SpawnEntry(type, entry.maxGroupSize(), entry.maxGroupSize())));
				return true;
			}
		}
		return false;
	}
}
