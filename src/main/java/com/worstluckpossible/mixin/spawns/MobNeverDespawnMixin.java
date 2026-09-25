package com.worstluckpossible.mixin.spawns;

import com.worstluckpossible.feature.MobPressureCache;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobNeverDespawnMixin {
	@Unique private static final Map<ServerWorld, Map<UUID, Boolean>> WORSTLUCK_PROTECTION_STATE = new WeakHashMap<>();

	@Inject(method = "checkDespawn", at = @At("HEAD"), cancellable = true)
	private void worstluck$despawnByDistance(CallbackInfo ci) {
		MobEntity self = (MobEntity) (Object) this;
		if (self.getType().getSpawnGroup() != SpawnGroup.MONSTER || self.isPersistent()
				|| self.cannotDespawn() || self.getEntityWorld().getDifficulty() == Difficulty.PEACEFUL
				|| !(self.getEntityWorld() instanceof ServerWorld world)) {
			return;
		}
		PlayerEntity closest = world.getClosestPlayer(self, -1.0D);
		if (closest == null) return;
		double distance = closest.squaredDistanceTo(self);
		if (distance <= MobPressureCache.NEAR_DISTANCE_SQUARED) {
			ci.cancel();
			return;
		}
		if (distance >= MobPressureCache.HARD_DISTANCE_SQUARED) {
			self.remove(Entity.RemovalReason.DISCARDED);
			ci.cancel();
			return;
		}

		Map<UUID, Boolean> state = WORSTLUCK_PROTECTION_STATE.computeIfAbsent(world, ignored -> new HashMap<>());
		boolean previous = state.getOrDefault(closest.getUuid(), false);
		boolean protect = MobPressureCache.get(world, closest).protectsDistantReservoir(previous);
		state.put(closest.getUuid(), protect);
		if (protect) ci.cancel();
	}
}
