package com.worstluckpossible.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.worstluckpossible.feature.MobPressureCache;
import java.util.Locale;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

/** Operator-only diagnostics. Gameplay controls live in the settings screen. */
public final class WorstLuckCommand {
	private WorstLuckCommand() {}

	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		dispatcher.register(CommandManager.literal("worstluck")
			.requires(CommandManager.requirePermissionLevel(CommandManager.GAMEMASTERS_CHECK))
			.then(CommandManager.literal("debug")
				.then(CommandManager.literal("spawning").executes(context -> showSpawningDebug(context.getSource())))));
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
