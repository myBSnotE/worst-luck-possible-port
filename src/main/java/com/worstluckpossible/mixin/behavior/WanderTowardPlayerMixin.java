package com.worstluckpossible.mixin.behavior;

import com.worstluckpossible.ai.ApproachNearestPlayerGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Adds the original priority-4 approach-player goal to path-aware mobs. */
@Mixin(MobEntity.class)
public class WanderTowardPlayerMixin {
	@Shadow protected GoalSelector goalSelector;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void worstluck$addApproachGoal(EntityType<? extends MobEntity> type, World world, CallbackInfo ci) {
		if ((Object) this instanceof PathAwareEntity pathAware) {
			goalSelector.add(4, new ApproachNearestPlayerGoal(pathAware));
		}
	}
}
