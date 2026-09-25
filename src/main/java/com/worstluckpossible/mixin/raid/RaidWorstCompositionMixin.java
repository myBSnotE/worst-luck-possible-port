package com.worstluckpossible.mixin.raid;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Maximizes vanilla wave bonuses and swaps one distant hostile for an extra raid witch. */
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
		MobEntity replacement = null;
		double farthestDistance = 32.0D * 32.0D;

		for (MobEntity candidate : world.getEntitiesByClass(
				MobEntity.class,
				new Box(raid.getCenter()).expand(128.0D),
				candidate -> candidate.isAlive()
						&& candidate.getType().getSpawnGroup() == SpawnGroup.MONSTER
						&& !(candidate instanceof RaiderEntity)
						&& !candidate.isPersistent()
						&& !candidate.cannotDespawn())) {
			PlayerEntity nearest = world.getClosestPlayer(candidate, -1.0D);
			if (nearest == null) {
				continue;
			}
			double distance = nearest.squaredDistanceTo(candidate);
			if (distance > farthestDistance && distance < 128.0D * 128.0D) {
				farthestDistance = distance;
				replacement = candidate;
			}
		}

		if (replacement == null) {
			return;
		}

		RaiderEntity witch = EntityType.WITCH.create(world, SpawnReason.EVENT);
		if (witch == null) {
			return;
		}
		raid.addRaider(world, raid.getGroupsSpawned(), witch, spawnPos, false);
		if (world.getEntity(witch.getUuid()) != null && witch.isAlive()) {
			replacement.remove(Entity.RemovalReason.DISCARDED);
		}
	}
}
