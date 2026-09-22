package com.worstluckpossible.mixin.behavior;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Gives newly constructed item entities an initial impulse generally away from
 * the nearest player. Player, dispenser, and other explicit throws replace this
 * constructor velocity afterwards, so their vanilla direction is preserved.
 */
@Mixin(ItemEntity.class)
public class ItemEntityAwayFromPlayerMixin {
	@Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void worstluck$scatterDefaultDrop(
			World world,
			double x,
			double y,
			double z,
			ItemStack stack,
			CallbackInfo ci
	) {
		worstluck$setInitialDropVelocity(world);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;DDD)V", at = @At("TAIL"))
	private void worstluck$scatterDirectedBlockDrop(
			World world,
			double x,
			double y,
			double z,
			ItemStack stack,
			double velocityX,
			double velocityY,
			double velocityZ,
			CallbackInfo ci
	) {
		worstluck$setInitialDropVelocity(world);
	}

	private void worstluck$setInitialDropVelocity(World world) {
		if (world.isClient()) {
			return;
		}

		ItemEntity self = (ItemEntity) (Object) this;
		if (self.isRemoved()) {
			return;
		}

		var player = world.getClosestPlayer(self, 64.0);
		if (player == null) {
			return;
		}

		double dx = self.getX() - player.getX();
		double dz = self.getZ() - player.getZ();
		double lengthSq = dx * dx + dz * dz;
		double baseAngle;
		if (lengthSq < 1.0E-4) {
			baseAngle = self.getRandom().nextDouble() * Math.PI * 2.0;
		}
		else {
			baseAngle = Math.atan2(dz, dx);
		}

		// Keep the impulse in the hemisphere away from the player, but add enough
		// variation that several drops from one block visibly scatter apart.
		double angle = baseAngle + (self.getRandom().nextDouble() - 0.5) * Math.PI * 2.0 / 3.0;
		self.setVelocity(new Vec3d(Math.cos(angle) * 0.25, 0.2, Math.sin(angle) * 0.25));
	}
}
