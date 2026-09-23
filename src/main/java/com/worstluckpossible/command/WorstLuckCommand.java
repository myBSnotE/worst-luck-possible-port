package com.worstluckpossible.command;

import com.mojang.brigadier.CommandDispatcher;
import com.worstluckpossible.feature.FishingLootMode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

/** Operator-only runtime controls for testing and co-op play. */
public final class WorstLuckCommand {
	private WorstLuckCommand() {
	}

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(
				CommandManager.literal("worstluck")
						.requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))
						.then(
								CommandManager.literal("fishing")
										.executes(context -> showFishingMode(context.getSource()))
										.then(
												CommandManager.literal("vanilla")
														.executes(context -> setFishingMode(context.getSource(), true))
										)
										.then(
												CommandManager.literal("boots")
														.executes(context -> setFishingMode(context.getSource(), false))
										)
						)
		);
	}

	private static int showFishingMode(ServerCommandSource source) {
		boolean forceBoots = FishingLootMode.shouldForceLeatherBoots(source.getServer());
		source.sendFeedback(
				() -> Text.literal(forceBoots
						? "Worst Luck fishing: every catch gives leather boots."
						: "Worst Luck fishing: vanilla loot is enabled for this server session."),
				false
		);
		return forceBoots ? 1 : 0;
	}

	private static int setFishingMode(ServerCommandSource source, boolean vanillaFishing) {
		FishingLootMode.setVanillaFishing(source.getServer(), vanillaFishing);
		source.sendFeedback(
				() -> Text.literal(vanillaFishing
						? "Vanilla fishing enabled until the world or server session ends."
						: "Forced leather boots enabled for fishing."),
				true
		);
		return 1;
	}
}
