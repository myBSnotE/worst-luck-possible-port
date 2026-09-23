package com.worstluckpossible.mixin.command;

import com.mojang.brigadier.CommandDispatcher;
import com.worstluckpossible.command.WorstLuckCommand;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class CommandManagerMixin {
	@Shadow
	@Final
	private CommandDispatcher<ServerCommandSource> dispatcher;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void worstluck$registerCommands(
			CommandManager.RegistrationEnvironment environment,
			CommandRegistryAccess commandRegistryAccess,
			CallbackInfo ci
	) {
		WorstLuckCommand.register(dispatcher);
	}
}
