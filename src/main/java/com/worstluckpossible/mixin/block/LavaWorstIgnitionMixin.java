package com.worstluckpossible.mixin.block;

import com.worstluckpossible.config.WorstLuckConfig;
import com.worstluckpossible.config.WorstLuckConfigManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Turns each lava random-tick opportunity into bounded guaranteed vanilla-range ignitions. */
@Mixin(LavaFluid.class)
public abstract class LavaWorstIgnitionMixin {
	@Unique private static final int WORSTLUCK_MAX_LAVA_IGNITIONS = 8;

	@Inject(method = "onRandomTick", at = @At("TAIL"))
	private void worstluck$forceLavaIgnitions(ServerWorld world, BlockPos pos, FluidState state,
			Random random, CallbackInfo ci) {
		if (!world.canFireSpread(pos)) {
			return;
		}
		if (WorstLuckConfigManager.get(world.getServer()).fireMode == WorstLuckConfig.FireMode.VANILLA) {
			return;
		}

		List<BlockPos> candidates = new ArrayList<>();

		// Vanilla's first branch can climb one or two blocks, moving at most one
		// horizontal block per climb. Its second branch uses the y=1, radius-1 area.
		worstluck$collectCandidates(world, pos, 1, 1, candidates);
		worstluck$collectCandidates(world, pos, 2, 2, candidates);

		worstluck$igniteRandom(world, random, candidates, WORSTLUCK_MAX_LAVA_IGNITIONS);
	}

	@Unique
	private static void worstluck$collectCandidates(ServerWorld world, BlockPos origin, int y, int radius,
			List<BlockPos> candidates) {
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				BlockPos target = origin.add(x, y, z);
				if (!world.isPosLoaded(target)
						|| !world.isAir(target)
						|| !worstluck$hasBurnableNeighbor(world, target)
						|| !world.getBlockState(target.down()).isBurnable()) {
					continue;
				}
				candidates.add(target.toImmutable());
			}
		}
	}

	@Unique
	private static boolean worstluck$hasBurnableNeighbor(ServerWorld world, BlockPos pos) {
		for (Direction direction : Direction.values()) {
			BlockPos neighbor = pos.offset(direction);
			if (world.isPosLoaded(neighbor) && world.getBlockState(neighbor).isBurnable()) {
				return true;
			}
		}
		return false;
	}

	@Unique
	private static int worstluck$igniteRandom(ServerWorld world, Random random,
			List<BlockPos> candidates, int budget) {
		while (budget > 0 && !candidates.isEmpty()) {
			BlockPos target = candidates.remove(random.nextInt(candidates.size()));
			if (world.isAir(target)
					&& worstluck$hasBurnableNeighbor(world, target)
					&& world.getBlockState(target.down()).isBurnable()) {
				world.setBlockState(target, AbstractFireBlock.getState(world, target), Block.NOTIFY_ALL);
				budget--;
			}
		}
		return budget;
	}

	/** Also constrain the two original vanilla lava-ignition writes in enhanced modes. */
	@Redirect(
			method = "onRandomTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"
			)
	)
	private boolean worstluck$rejectLavaFireOverNonFlammableSupport(ServerWorld world,
			BlockPos target, BlockState state) {
		WorstLuckConfig.FireMode mode = WorstLuckConfigManager.get(world.getServer()).fireMode;
		if (mode != WorstLuckConfig.FireMode.VANILLA
				&& !world.getBlockState(target.down()).isBurnable()) {
			return false;
		}
		return world.setBlockState(target, state);
	}
}
