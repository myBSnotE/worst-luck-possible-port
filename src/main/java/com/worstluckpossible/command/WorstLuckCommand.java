package com.worstluckpossible.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.worstluckpossible.feature.FishingLootMode;
import com.worstluckpossible.feature.LightningRateMode;
import com.worstluckpossible.feature.MobPressureCache;
import java.util.Locale;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/** Operator-only runtime controls and diagnostics for testing and co-op play. */
public final class WorstLuckCommand {
	private WorstLuckCommand() {}

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("worstluck")
			.requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))
			.then(CommandManager.literal("fishing")
				.executes(context -> showFishingMode(context.getSource()))
				.then(CommandManager.literal("vanilla").executes(context -> setFishingMode(context.getSource(), true)))
				.then(CommandManager.literal("boots").executes(context -> setFishingMode(context.getSource(), false))))
			.then(CommandManager.literal("lightning")
				.executes(context -> showLightningMode(context.getSource()))
				.then(CommandManager.literal("reduced").executes(context -> setLightningMode(context.getSource(), true)))
				.then(CommandManager.literal("full").executes(context -> setLightningMode(context.getSource(), false))))
			.then(CommandManager.literal("debug")
				.then(CommandManager.literal("spawning").executes(context -> showSpawningDebug(context.getSource())))));
	}

	private static int showFishingMode(ServerCommandSource source) {
		boolean forceBoots = FishingLootMode.shouldForceLeatherBoots(source.getServer());
		source.sendFeedback(() -> Text.literal(forceBoots
			? "Worst Luck fishing: every catch gives leather boots."
			: "Worst Luck fishing: vanilla loot is enabled for this server session."), false);
		return forceBoots ? 1 : 0;
	}

	private static int setFishingMode(ServerCommandSource source, boolean vanillaFishing) {
		FishingLootMode.setVanillaFishing(source.getServer(), vanillaFishing);
		source.sendFeedback(() -> Text.literal(vanillaFishing
			? "Vanilla fishing enabled until the world or server session ends."
			: "Forced leather boots enabled for fishing."), true);
		return 1;
	}

	private static int showLightningMode(ServerCommandSource source) {
		boolean reduced = LightningRateMode.isReduced(source.getServer());
		source.sendFeedback(() -> Text.literal(reduced
			? "Worst Luck lightning: reduced mode (5% chance per ticking chunk each tick)."
			: "Worst Luck lightning: full mode (every ticking chunk each tick)."), false);
		return reduced ? 1 : 0;
	}

	private static int setLightningMode(ServerCommandSource source, boolean reduced) {
		LightningRateMode.setReduced(source.getServer(), reduced);
		source.sendFeedback(() -> Text.literal(reduced
			? "Reduced lightning enabled: 5% chance per ticking chunk each tick until the world or server session ends."
			: "Full Worst Luck lightning restored: every ticking chunk attempts a strike each tick."), true);
		return 1;
	}

	private static int showSpawningDebug(ServerCommandSource source) throws CommandSyntaxException {
		ServerPlayerEntity player = source.getPlayerOrThrow();
		MobPressureCache.invalidate(source.getWorld(), player);
		MobPressureCache.Snapshot snapshot = MobPressureCache.get(source.getWorld(), player);
		MobEntity farthest = snapshot.farthestReplaceable();
		double farthestDistance = farthest == null ? -1.0D : Math.sqrt(player.squaredDistanceTo(farthest));
		String message = String.format(Locale.ROOT,
			"Worst Luck spawning: mobs=%d, persistent=%d, hostiles=%d, near(<=32)=%d, distant=%d, replacementBudget=%d, reservoirProtected=%s, farthestReplaceable=%s",
			snapshot.totalMobs(), snapshot.persistentMobs(), snapshot.totalHostiles(), snapshot.nearHostiles(),
			Math.max(0, snapshot.totalHostiles() - snapshot.nearHostiles()), snapshot.replacementBudget(),
			snapshot.protectsDistantReservoir(false),
			farthest == null ? "none" : String.format(Locale.ROOT, "%.1f blocks", farthestDistance));
		source.sendFeedback(() -> Text.literal(message), false);
		return snapshot.totalHostiles();
	}
}
