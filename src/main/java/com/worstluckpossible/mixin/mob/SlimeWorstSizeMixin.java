package com.worstluckpossible.mixin.mob;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.jspecify.annotations.Nullable;

/** Natural slimes and magma cubes always use vanilla's largest natural size. */
@Mixin(SlimeEntity.class)
public class SlimeWorstSizeMixin {
	@Inject(method = "initialize", at = @At("RETURN"))
	private void worstluck$forceLargestNaturalSize(ServerWorldAccess world, LocalDifficulty difficulty,
			SpawnReason spawnReason, @Nullable EntityData entityData,
			CallbackInfoReturnable<EntityData> cir) {
		if (spawnReason == SpawnReason.NATURAL) {
			((SlimeEntity) (Object) this).setSize(4, true);
		}
	}
}
