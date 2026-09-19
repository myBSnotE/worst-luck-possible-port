package com.worstluckpossible.mixin.spawns;

import net.minecraft.entity.EntityType;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/** Harmless nether mobs are replaced with the worst possible neighbour: a ghast. */
@Mixin(SpawnHelper.class)
public class SpawnHelperSubstituteMixin {
	@ModifyVariable(method = "createMob", at = @At("HEAD"), argsOnly = true, index = 1, require = 0)
	private static EntityType<?> worstluck$substitute(EntityType<?> type) {
		if (type == EntityType.ZOMBIFIED_PIGLIN || type == EntityType.STRIDER) {
			return EntityType.GHAST;
		}
		return type;
	}
}
