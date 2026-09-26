package com.worstluckpossible.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.worstluckpossible.WorstLuckPossible;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.WorldSavePath;

public final class WorstLuckConfigManager {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final String FILE_NAME = "worst-luck-possible.json";
	private static final Map<MinecraftServer, WorstLuckConfig> SERVER_CONFIGS =
			Collections.synchronizedMap(new WeakHashMap<>());
	private static WorstLuckConfig defaults;

	private WorstLuckConfigManager() {}

	public static synchronized WorstLuckConfig defaults() {
		if (defaults == null) defaults = read(globalPath(), new WorstLuckConfig(), true);
		return defaults;
	}

	public static synchronized void saveDefaults(WorstLuckConfig config) {
		defaults = config.copy().normalize();
		write(globalPath(), defaults);
	}

	public static WorstLuckConfig get(MinecraftServer server) {
		WorstLuckConfig config = SERVER_CONFIGS.get(server);
		return config != null ? config : defaults();
	}

	public static void loadWorld(MinecraftServer server) {
		Path path = worldPath(server);
		WorstLuckConfig config = Files.exists(path)
				? read(path, defaults(), false)
				: defaults().copy();
		SERVER_CONFIGS.put(server, config.normalize());
		write(path, config);
	}

	public static void saveWorld(MinecraftServer server, WorstLuckConfig config) {
		WorstLuckConfig normalized = config.copy().normalize();
		SERVER_CONFIGS.put(server, normalized);
		write(worldPath(server), normalized);
	}

	public static void unload(MinecraftServer server) {
		WorstLuckConfig config = SERVER_CONFIGS.remove(server);
		if (config != null) write(worldPath(server), config);
	}

	public static boolean canEdit(ServerPlayerEntity player) {
		MinecraftServer server = player.getEntityWorld().getServer();
		boolean operator = CommandManager.GAMEMASTERS_CHECK.allows(player.getPermissions());
		if (operator) return true;
		WorstLuckConfig config = get(server);
		if (config.responsibleMode || !server.isSingleplayer() || server.getHostProfile() == null) return false;
		return server.getHostProfile().id().equals(player.getGameProfile().id());
	}

	public static String toJson(WorstLuckConfig config) {
		return GSON.toJson(config.copy().normalize());
	}

	public static WorstLuckConfig fromJson(String json) {
		try {
			WorstLuckConfig config = GSON.fromJson(json, WorstLuckConfig.class);
			return config == null ? new WorstLuckConfig() : config.normalize();
		} catch (RuntimeException exception) {
			WorstLuckPossible.LOGGER.warn("Rejected invalid Worst Luck config JSON", exception);
			return new WorstLuckConfig();
		}
	}

	private static Path globalPath() {
		return FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
	}

	private static Path worldPath(MinecraftServer server) {
		return server.getSavePath(WorldSavePath.ROOT).resolve(FILE_NAME);
	}

	private static WorstLuckConfig read(Path path, WorstLuckConfig fallback, boolean create) {
		if (Files.exists(path)) {
			try (Reader reader = Files.newBufferedReader(path)) {
				WorstLuckConfig config = GSON.fromJson(reader, WorstLuckConfig.class);
				if (config != null) return config.normalize();
			} catch (IOException | RuntimeException exception) {
				WorstLuckPossible.LOGGER.error("Could not read config {}", path, exception);
			}
		}
		WorstLuckConfig result = fallback.copy().normalize();
		if (create) write(path, result);
		return result;
	}

	private static void write(Path path, WorstLuckConfig config) {
		try {
			Files.createDirectories(path.getParent());
			Path temporary = path.resolveSibling(path.getFileName() + ".tmp");
			try (Writer writer = Files.newBufferedWriter(temporary)) {
				GSON.toJson(config.normalize(), writer);
			}
			try {
				Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			} catch (IOException ignored) {
				Files.move(temporary, path, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException exception) {
			WorstLuckPossible.LOGGER.error("Could not write config {}", path, exception);
		}
	}
}
