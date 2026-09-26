package com.worstluckpossible.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireBlock.class)
public interface FireBlockAccessor {
	@Invoker("getSpreadChance")
	int worstluck$getSpreadChance(BlockState state);

	@Invoker("trySpreadingFire")
	void worstluck$trySpreadingFire(World world, BlockPos pos, int spreadFactor, Random random, int currentAge);
}
