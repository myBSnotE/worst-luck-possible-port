package com.worstluckpossible.mixin.block;

import com.worstluckpossible.config.WorstLuckConfig;
import com.worstluckpossible.config.WorstLuckConfigManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Consumes all adjacent fuel in the same tick whenever ordinary fire is extinguished. */
@Mixin(WorldChunk.class)
public abstract class FireExtinguishConsumesFuelMixin {
	@Shadow @Final World world;

	@Unique
	private static final ThreadLocal<Boolean> WORSTLUCK_CONSUMING_FUEL =
			ThreadLocal.withInitial(() -> false);

	@Shadow
	public abstract BlockState getBlockState(BlockPos pos);

	@Inject(method = "setBlockState", at = @At("HEAD"))
	private void worstluck$consumeFuelOnExtinguish(BlockPos pos, BlockState newState, int flags,
			CallbackInfoReturnable<BlockState> cir) {
		if (!(world instanceof ServerWorld serverWorld)
				|| WorstLuckConfigManager.get(serverWorld.getServer()).fireMode
						!= WorstLuckConfig.FireMode.ETERNAL
				|| WORSTLUCK_CONSUMING_FUEL.get()
				|| !getBlockState(pos).isOf(Blocks.FIRE)
				|| newState.isOf(Blocks.FIRE)) {
			return;
		}

		FireBlockAccessor fire = (FireBlockAccessor)(Object)Blocks.FIRE;
		WORSTLUCK_CONSUMING_FUEL.set(true);
		try {
			for (Direction direction : Direction.values()) {
				BlockPos fuelPos = pos.offset(direction);
				BlockState fuelState = serverWorld.getBlockState(fuelPos);
				if (fire.worstluck$getSpreadChance(fuelState) <= 0) continue;

				if (fuelState.getBlock() instanceof TntBlock) {
					TntBlock.primeTnt(serverWorld, fuelPos);
				}
				serverWorld.removeBlock(fuelPos, false);
			}
		} finally {
			WORSTLUCK_CONSUMING_FUEL.set(false);
		}
	}
}
