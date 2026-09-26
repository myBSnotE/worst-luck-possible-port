package com.worstluckpossible.mixin.loot;

import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.spawner.TrialSpawnerLogic;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Trial spawners always eject the least useful food reward instead of keys or potions. */
@Mixin(TrialSpawnerLogic.class)
public class TrialSpawnerBakedPotatoMixin {
	@Inject(method = "ejectLootTable", at = @At("HEAD"), cancellable = true)
	private void worstluck$alwaysEjectBakedPotato(ServerWorld world, BlockPos pos,
			RegistryKey<LootTable> lootTable, CallbackInfo ci) {
		ItemDispenserBehavior.spawnItem(world, new ItemStack(Items.BAKED_POTATO), 2, Direction.UP,
				Vec3d.ofBottomCenter(pos).offset(Direction.UP, 1.2));
		world.syncWorldEvent(WorldEvents.TRIAL_SPAWNER_EJECTS_ITEM, pos, 0);
		ci.cancel();
	}
}
