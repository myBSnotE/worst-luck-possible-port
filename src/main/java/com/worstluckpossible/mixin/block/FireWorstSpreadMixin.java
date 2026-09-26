package com.worstluckpossible.mixin.block;

import com.worstluckpossible.config.WorstLuckConfig;
import com.worstluckpossible.config.WorstLuckConfigManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Forces persistent, destructive fire while bounding work per source tick. */
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

	@Shadow
	private boolean areBlocksAroundFlammable(net.minecraft.world.BlockView world, BlockPos pos) {
		throw new AssertionError();
	}

	/** Fuelled fire always uses the youngest possible age, so it lasts until extinguished. */
	@ModifyVariable(method = "scheduledTick", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private BlockState worstluck$keepFuelledFireYoung(BlockState state, BlockState originalState,
			ServerWorld world, BlockPos pos, Random random) {
		return worstluck$isEternal(world) && areBlocksAroundFlammable(world, pos)
				? state.with(FireBlock.AGE, 0) : state;
	}

	/**
	 * Vanilla derives a new age immediately after reading the method argument and
	 * can therefore store age 1 even when the argument was reset to age 0 above.
	 * Clamp that source-fire write as well.
	 */
	@Redirect(
			method = "scheduledTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
					ordinal = 0
			)
	)
	private boolean worstluck$storeFuelledFireAtAgeZero(ServerWorld world, BlockPos pos,
			BlockState state, int flags) {
		if (worstluck$isEternal(world) && areBlocksAroundFlammable(world, pos) && state.isOf(Blocks.FIRE)) {
			state = state.with(FireBlock.AGE, 0);
		}
		return world.setBlockState(pos, state, flags);
	}

	/** Do not consume fuel through vanilla's random direct-burn roll. Extinguishing does that instead. */
	@Redirect(
			method = "scheduledTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/FireBlock;trySpreadingFire(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/util/math/random/Random;I)V"
			)
	)
	private void worstluck$preserveBurningFuel(FireBlock instance, World world, BlockPos target,
			int spreadFactor, Random random, int currentAge) {
		if (world instanceof ServerWorld serverWorld && worstluck$isEternal(serverWorld)) {
			// Nearby fuel burns for the maximum duration and is consumed only when
			// this fire is explicitly extinguished.
			return;
		}
		((FireBlockAccessor) instance).worstluck$trySpreadingFire(world, target, spreadFactor, random, currentAge);
	}

	/**
	 * Runs before vanilla's early exits, allowing fire to bridge onto non-flammable
	 * supports when an otherwise unreachable flammable block can sustain the new fire.
	 */
	@Inject(method = "scheduledTick", at = @At("HEAD"))
	private void worstluck$forceDangerousSpread(BlockState state, ServerWorld world, BlockPos pos,
			Random random, CallbackInfo ci) {
		if (worstluck$isVanilla(world)
				|| !world.canFireSpread(pos)
				|| !world.getBlockState(pos).isOf(Blocks.FIRE)) {
			return;
		}

		BlockState liveState = world.getBlockState(pos);
		if (areBlocksAroundFlammable(world, pos) && liveState.get(FireBlock.AGE) != 0) {
			world.setBlockState(pos, liveState.with(FireBlock.AGE, 0), Block.NOTIFY_ALL);
		}

		int budget = WORSTLUCK_MAX_SPREADS_PER_TICK;

		// First ignite the feet of nearby players and passive mobs, but only when
		// vanilla's burn-chance lookup finds adjacent fuel for that exact position.
		List<LivingEntity> vulnerable = world.getEntitiesByClass(LivingEntity.class,
				new Box(pos).expand(1.5D, 4.0D, 1.5D),
				entity -> entity.isAlive() && (entity instanceof PlayerEntity || entity instanceof PassiveEntity));
		for (LivingEntity entity : vulnerable) {
			if (budget == 0) break;
			BlockPos target = entity.getBlockPos();
			if (world.isAir(target)
					&& getBurnChance(world, target) > 0) {
				world.setBlockState(target, getStateWithAge(world, target, 0), Block.NOTIFY_ALL);
				budget--;
			}
		}

		List<BlockPos> nonFlammableSupports = new ArrayList<>();
		List<BlockPos> otherTargets = new ArrayList<>();

		// Use the complete vanilla long-range volume. Air above a solid non-flammable
		// support is explicitly eligible whenever nearby fuel can sustain fire there.
		for (int y = -1; y <= 4; y++) {
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					if (x == 0 && y == 0 && z == 0) continue;
					BlockPos target = pos.add(x, y, z);
					if (!world.isAir(target) || getBurnChance(world, target) <= 0) continue;

					BlockPos floor = target.down();
					BlockState floorState = world.getBlockState(floor);
					if (floorState.isSideSolidFullSquare(world, floor, Direction.UP)
							&& getSpreadChance(floorState) == 0) {
						nonFlammableSupports.add(target.toImmutable());
					} else {
						otherTargets.add(target.toImmutable());
					}
				}
			}
		}

		worstluck$shuffle(nonFlammableSupports, random);
		worstluck$shuffle(otherTargets, random);
		budget = worstluck$ignite(world, nonFlammableSupports, budget);
		worstluck$ignite(world, otherTargets, budget);
	}

	@Unique
	private int worstluck$ignite(ServerWorld world, List<BlockPos> targets, int budget) {
		for (BlockPos target : targets) {
			if (budget == 0) break;
			if (world.isAir(target) && getBurnChance(world, target) > 0) {
				world.setBlockState(target, getStateWithAge(world, target, 0), Block.NOTIFY_ALL);
				budget--;
			}
		}
		return budget;
	}

	@Unique
	private static <T> void worstluck$shuffle(List<T> values, Random random) {
		for (int i = values.size() - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			T value = values.get(i);
			values.set(i, values.get(j));
			values.set(j, value);
		}
	}

	@Unique
	private static boolean worstluck$isEternal(ServerWorld world) {
		return WorstLuckConfigManager.get(world.getServer()).fireMode == WorstLuckConfig.FireMode.ETERNAL;
	}

	@Unique
	private static boolean worstluck$isVanilla(ServerWorld world) {
		return WorstLuckConfigManager.get(world.getServer()).fireMode == WorstLuckConfig.FireMode.VANILLA;
	}
}
