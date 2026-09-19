package com.worstluckpossible.mixin.spawns;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** No passive animals are placed during world generation. */
@Mixin(SpawnHelper.class)
public class SpawnHelperNoAnimalsMixin {
	@Inject(method = "populateEntities", at = @At("HEAD"), cancellable = true, require = 0)
	private static void worstluck$noNaturalAnimals(
			ServerWorldAccess world,
			RegistryEntry<Biome> biome,
			ChunkPos chunkPos,
			Random random,
			CallbackInfo ci) {
		ci.cancel();
	}
}
