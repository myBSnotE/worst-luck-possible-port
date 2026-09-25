package com.worstluckpossible.ai;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

/** Approaches players while sharing a bounded per-world path-search budget. */
public class ApproachNearestPlayerGoal extends Goal {
	private static final Map<ServerWorld, PathBudget> PATH_BUDGETS = new WeakHashMap<>();
	private final PathAwareEntity mob;
	private PlayerEntity player;
	private Vec3d targetPos;
	private int nextSearchAge;
	private int nextRefreshAge;

	public ApproachNearestPlayerGoal(PathAwareEntity mob) {
		this.mob = mob;
		this.nextSearchAge = mob.age + (mob.getId() & 15);
		setControls(EnumSet.of(Control.MOVE));
	}

	@Override
	public boolean canStart() {
		if (mob.getTarget() != null || !(mob.getEntityWorld() instanceof ServerWorld world)
				|| mob.age < nextSearchAge) {
			return false;
		}
		nextSearchAge = mob.age + 12 + (mob.getId() & 7);
		if (!claimPathSearch(world)) {
			return false;
		}

		PlayerEntity nearest = world.getPlayers().stream()
				.filter(candidate -> candidate.isAlive() && !candidate.isSpectator() && !candidate.isCreative())
				.min(Comparator.comparingDouble(mob::squaredDistanceTo))
				.orElse(null);
		if (nearest == null) {
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
		nextRefreshAge = mob.age + 15 + (mob.getId() & 7);
		mob.getNavigation().startMovingTo(targetPos.x, targetPos.y, targetPos.z, 1.0);
	}

	@Override
	public void tick() {
		if (player == null || targetPos == null || mob.age < nextRefreshAge
				|| mob.squaredDistanceTo(targetPos) >= 2.0
				|| !(mob.getEntityWorld() instanceof ServerWorld world)
				|| !claimPathSearch(world)) {
			return;
		}
		nextRefreshAge = mob.age + 15 + (mob.getId() & 7);
		Vec3d next = NoPenaltyTargeting.findTo(mob, 10, 7, player.getEntityPos(), Math.PI / 2.0);
		if (next != null) {
			targetPos = next;
			mob.getNavigation().startMovingTo(next.x, next.y, next.z, 1.0);
		}
	}

	@Override
	public void stop() {
		player = null;
		targetPos = null;
		nextSearchAge = mob.age + 8 + (mob.getId() & 7);
		mob.getNavigation().stop();
	}

	private static boolean claimPathSearch(ServerWorld world) {
		long tick = world.getTime();
		PathBudget budget = PATH_BUDGETS.computeIfAbsent(world, ignored -> new PathBudget());
		if (budget.tick != tick) {
			budget.tick = tick;
			budget.used = 0;
		}
		int limit = Math.max(4, Math.min(24, world.getPlayers().size() * 4));
		if (budget.used >= limit) {
			return false;
		}
		budget.used++;
		return true;
	}

	private static final class PathBudget {
		private long tick = Long.MIN_VALUE;
		private int used;
	}
}
