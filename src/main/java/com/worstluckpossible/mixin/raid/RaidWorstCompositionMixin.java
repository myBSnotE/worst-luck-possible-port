package com.worstluckpossible.mixin.raid;

import com.worstluckpossible.feature.MobPressureCache;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raid.class)
public class RaidWorstCompositionMixin {
	@Redirect(method = "getBonusCount", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"))
	private int worstluck$maximumBonusCount(Random random, int bound) {
		return Math.max(0, bound - 1);
	}

	@Inject(method = "spawnNextWave", at = @At("RETURN"))
	private void worstluck$replaceDistantHostileWithWitch(ServerWorld world, BlockPos spawnPos, CallbackInfo ci) {
		Raid raid = (Raid) (Object) this;
		PlayerEntity player = world.getClosestPlayer(
				raid.getCenter().getX() + 0.5D, raid.getCenter().getY() + 0.5D,
				raid.getCenter().getZ() + 0.5D, 128.0D, false);
		if (player == null) return;
		MobEntity replacement = MobPressureCache.get(world, player).farthestReplaceable();
		if (replacement == null || !replacement.isAlive()) return;

		RaiderEntity witch = EntityType.WITCH.create(world, SpawnReason.EVENT);
		if (witch == null) return;
		raid.addRaider(world, raid.getGroupsSpawned(), witch, spawnPos, false);
		if (world.getEntity(witch.getUuid()) != null && witch.isAlive()) {
			replacement.remove(Entity.RemovalReason.DISCARDED);
			MobPressureCache.invalidate(world, player);
		}
	}
}
