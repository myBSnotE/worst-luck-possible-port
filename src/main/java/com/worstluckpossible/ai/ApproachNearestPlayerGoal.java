package com.worstluckpossible.ai;

import java.util.EnumSet;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

/** 1.21 implementation of the original mod's approach-player goal. */
public class ApproachNearestPlayerGoal extends Goal {
	private final PathAwareEntity mob;
	private PlayerEntity player;
	private Vec3d targetPos;

	public ApproachNearestPlayerGoal(PathAwareEntity mob) {
		this.mob = mob;
		setControls(EnumSet.of(Control.MOVE));
	}

	@Override
	public boolean canStart() {
		if (mob.getTarget() != null || mob.isPersistent() || mob.cannotDespawn()) {
			return false;
		}
		PlayerEntity nearest = mob.getEntityWorld().getClosestPlayer(mob, -1.0);
		if (nearest == null || nearest.isSpectator() || nearest.isCreative()) {
			return false;
		}
		Vec3d pos = NoPenaltyTargeting.findTo(mob, 10, 7, nearest.getEntityPos(), Math.PI / 2.0);
		if (pos == null) {
			return false;
		}
		player = nearest;
		targetPos = pos;
		return true;
	}

	@Override
	public boolean shouldContinue() {
		return mob.getTarget() == null && player != null && player.isAlive()
				&& !player.isSpectator() && !player.isCreative() && !mob.getNavigation().isIdle();
	}

	@Override
	public void start() {
		mob.getNavigation().startMovingTo(targetPos.x, targetPos.y, targetPos.z, 1.0);
	}

	@Override
	public void tick() {
		if (player != null && targetPos != null && mob.squaredDistanceTo(targetPos) < 2.0) {
			Vec3d next = NoPenaltyTargeting.findTo(mob, 10, 7, player.getEntityPos(), Math.PI / 2.0);
			if (next != null) {
				targetPos = next;
				mob.getNavigation().startMovingTo(next.x, next.y, next.z, 1.0);
			}
		}
	}

	@Override
	public void stop() {
		player = null;
		targetPos = null;
		mob.getNavigation().stop();
	}
}
