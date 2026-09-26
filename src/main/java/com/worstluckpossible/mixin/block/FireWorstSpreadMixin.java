package com.worstluckpossible.mixin.block;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Forces destructive vanilla-range fire spread while bounding work per source tick. */
@Mixin(FireBlock.class)
public abstract class FireWorstSpreadMixin {
	@Unique private static final int WORSTLUCK_MAX_SPREADS_PER_TICK = 8;

	@Shadow
	private int getSpreadChance(BlockState state) {
		throw new AssertionError();
	}

	@Shadow
	private int getBurnChance(WorldView world, BlockPos pos) {
		throw new AssertionError();
	}

	@Shadow
	private BlockState getStateWithAge(WorldView world, BlockPos pos, int age) {
		throw new AssertionError();
	}

	@Inject(method = "scheduledTick", at = @At("TAIL"))
	private void worstluck$forceDangerousSpread(BlockState state, ServerWorld world, BlockPos pos,
			Random random, CallbackInfo ci) {
		if (!world.canFireSpread(pos) || !world.getBlockState(pos).isOf(Blocks.FIRE)) {
			return;
		}

		int age = world.getBlockState(pos).get(FireBlock.AGE);
		int budget = WORSTLUCK_MAX_SPREADS_PER_TICK;

		// First ignite the feet of nearby players and passive mobs even when they stand
		// on otherwise non-flammable solid blocks.
		List<LivingEntity> vulnerable = world.getEntitiesByClass(LivingEntity.class,
				new Box(pos).expand(1.5D, 4.0D, 1.5D),
				entity -> entity.isAlive() && (entity instanceof PlayerEntity || entity instanceof PassiveEntity));
		for (LivingEntity entity : vulnerable) {
			if (budget == 0) break;
			BlockPos target = entity.getBlockPos();
			BlockPos floor = target.down();
			BlockState floorState = world.getBlockState(floor);
			if (world.isAir(target)
					&& floorState.isSideSolidFullSquare(world, floor, Direction.UP)
					&& getSpreadChance(floorState) == 0) {
				world.setBlockState(target, getStateWithAge(world, target, age), Block.NOTIFY_ALL);
				budget--;
			}
		}

		// Every direct vanilla spread attempt that is still available succeeds.
		for (Direction direction : Direction.values()) {
			if (budget == 0) break;
			BlockPos target = pos.offset(direction);
			BlockState targetState = world.getBlockState(target);
			if (getSpreadChance(targetState) <= 0) continue;
			world.setBlockState(target, getStateWithAge(world, target, age), Block.NOTIFY_ALL);
			if (targetState.getBlock() instanceof TntBlock) {
				TntBlock.primeTnt(world, target);
			}
			budget--;
		}

		// Fill valid air positions in the same volume used by vanilla long-range spread.
		for (int y = -1; y <= 4 && budget > 0; y++) {
			for (int x = -1; x <= 1 && budget > 0; x++) {
				for (int z = -1; z <= 1 && budget > 0; z++) {
					if (x == 0 && y == 0 && z == 0) continue;
					BlockPos target = pos.add(x, y, z);
					if (world.isAir(target) && getBurnChance(world, target) > 0) {
						world.setBlockState(target, getStateWithAge(world, target, age), Block.NOTIFY_ALL);
						budget--;
					}
				}
			}
		}
	}
}
