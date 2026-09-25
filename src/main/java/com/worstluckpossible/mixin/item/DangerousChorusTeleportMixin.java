package com.worstluckpossible.mixin.item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.TeleportRandomlyConsumeEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Tries the same sixteen vanilla chorus candidates, but in worst-first order. */
@Mixin(TeleportRandomlyConsumeEffect.class)
public class DangerousChorusTeleportMixin {
	@Inject(method = "onConsume", at = @At("HEAD"), cancellable = true)
	private void worstluck$chooseMostDangerousCandidate(World world, ItemStack stack, LivingEntity user,
			CallbackInfoReturnable<Boolean> cir) {
		if (!(world instanceof ServerWorld serverWorld) || !(user instanceof PlayerEntity player)) {
			return;
		}

		float diameter = ((TeleportRandomlyConsumeEffect) (Object) this).diameter();
		List<WorstluckCandidate> candidates = new ArrayList<>(16);
		for (int i = 0; i < 16; i++) {
			double x = user.getX() + (user.getRandom().nextDouble() - 0.5D) * diameter;
			double y = MathHelper.clamp(
					user.getY() + (user.getRandom().nextDouble() - 0.5D) * diameter,
					(double) world.getBottomY(),
					(double) (world.getBottomY() + serverWorld.getLogicalHeight() - 1));
			double z = user.getZ() + (user.getRandom().nextDouble() - 0.5D) * diameter;
			double score = worstluck$dangerScore(serverWorld, player, x, y, z)
					+ user.getRandom().nextDouble() * 6.0D;
			candidates.add(new WorstluckCandidate(x, y, z, score));
		}
		candidates.sort(Comparator.comparingDouble(WorstluckCandidate::score).reversed());

		if (user.hasVehicle()) {
			user.stopRiding();
		}
		boolean teleported = false;
		for (WorstluckCandidate candidate : candidates) {
			Vec3d from = user.getEntityPos();
			if (user.teleport(candidate.x(), candidate.y(), candidate.z(), true)) {
				world.emitGameEvent(GameEvent.TELEPORT, from, GameEvent.Emitter.of(user));
				world.playSound(null, user.getX(), user.getY(), user.getZ(),
						SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS);
				user.onLanding();
				teleported = true;
				break;
			}
		}
		if (teleported) {
			player.clearCurrentExplosion();
		}
		cir.setReturnValue(teleported);
	}

	@Unique
	private static double worstluck$dangerScore(ServerWorld world, PlayerEntity player,
			double x, double y, double z) {
		BlockPos center = BlockPos.ofFloored(x, y, z);
		double score = player.squaredDistanceTo(x, y, z) * 0.12D;
		score += Math.max(0.0D, player.getY() - y) * 3.0D;
		score += Math.max(0, 15 - world.getLightLevel(center)) * 1.5D;

		int hostileCount = world.getEntitiesByClass(
				HostileEntity.class,
				new Box(center).expand(8.0D),
				entity -> entity.isAlive()).size();
		score += hostileCount * 18.0D;

		for (BlockPos pos : BlockPos.iterate(center.add(-3, -2, -3), center.add(3, 2, 3))) {
			BlockState state = world.getBlockState(pos);
			if (state.isOf(Blocks.LAVA) || state.isOf(Blocks.FIRE) || state.isOf(Blocks.SOUL_FIRE)) {
				score += 20.0D;
			} else if (state.isOf(Blocks.MAGMA_BLOCK) || state.isOf(Blocks.CACTUS)
					|| state.isOf(Blocks.CAMPFIRE) || state.isOf(Blocks.SOUL_CAMPFIRE)
					|| state.isOf(Blocks.SWEET_BERRY_BUSH) || state.isOf(Blocks.POWDER_SNOW)
					|| state.isOf(Blocks.POINTED_DRIPSTONE)) {
				score += 7.0D;
			}
		}

		for (int depth = 1; depth <= 5; depth++) {
			if (world.getBlockState(center.down(depth)).isAir()) {
				score += depth * 2.5D;
			} else {
				break;
			}
		}
		return score;
	}

	@Unique
	private record WorstluckCandidate(double x, double y, double z, double score) {
	}
}
