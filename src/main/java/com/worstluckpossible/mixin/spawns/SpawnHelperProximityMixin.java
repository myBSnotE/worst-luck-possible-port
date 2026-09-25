package com.worstluckpossible.mixin.spawns;

import com.worstluckpossible.feature.MobPressureCache;
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

/** Prefers hostile natural spawns close to a real player without deadlocking an empty mob cap. */
@Mixin(SpawnHelper.class)
public class SpawnHelperProximityMixin {
	@Inject(method = "canSpawn", at = @At("HEAD"), cancellable = true)
	private static void worstluck$monstersNearPlayer(ServerWorld world, SpawnGroup group,
			StructureAccessor structures, ChunkGenerator generator, SpawnSettings.SpawnEntry entry,
			BlockPos.Mutable pos, double squaredDistance, CallbackInfoReturnable<Boolean> cir) {
		if (group != SpawnGroup.MONSTER) {
			return;
		}
		if (squaredDistance < 576.0D) {
			cir.setReturnValue(false);
			return;
		}

		PlayerEntity player = world.getClosestPlayer(
				pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, -1.0D, false);
		if (player == null) {
			cir.setReturnValue(false);
			return;
		}

		if (squaredDistance <= MobPressureCache.NEAR_DISTANCE_SQUARED) {
			// Preferred 24-32 block band keeps the original vertical targeting rule.
			if (Math.abs(player.getBlockY() - pos.getY()) > 16) {
				cir.setReturnValue(false);
			}
			return;
		}

		// If this player's hostile population is empty, allow one ordinary vanilla-range
		// spawn to seed the cycle. The runner invalidates the snapshot immediately after
		// that mob appears, so further attempts return to the preferred close band.
		if (MobPressureCache.get(world, player).totalHostiles() > 0) {
			cir.setReturnValue(false);
		}
	}
}
