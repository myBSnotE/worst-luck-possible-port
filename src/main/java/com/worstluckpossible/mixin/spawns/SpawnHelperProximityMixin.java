package com.worstluckpossible.mixin.spawns;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Restricts hostile natural spawns to the original 24-32 block ring and +/-16 Y. */
@Mixin(SpawnHelper.class)
public class SpawnHelperProximityMixin {
	@Inject(method = "canSpawn", at = @At("HEAD"), cancellable = true)
	private static void worstluck$monstersNearPlayer(ServerWorld world, SpawnGroup group,
			StructureAccessor structures, ChunkGenerator generator, SpawnSettings.SpawnEntry entry,
			BlockPos.Mutable pos, double squaredDistance, CallbackInfoReturnable<Boolean> cir) {
		if (group != SpawnGroup.MONSTER) {
			return;
		}
		if (squaredDistance > 1024.0) {
			cir.setReturnValue(false);
			return;
		}
		PlayerEntity player = world.getClosestPlayer(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, -1.0, false);
		if (player == null || Math.abs(player.getBlockY() - pos.getY()) > 16) {
			cir.setReturnValue(false);
		}
	}
}
