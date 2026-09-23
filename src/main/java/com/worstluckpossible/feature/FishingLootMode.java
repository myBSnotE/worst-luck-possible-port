package com.worstluckpossible.feature;

import net.minecraft.server.MinecraftServer;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Holds the temporary fishing override for each running server instance.
 * A newly opened world or restarted dedicated server uses forced boots again.
 */
public final class FishingLootMode {
	private static final Set<MinecraftServer> VANILLA_FISHING_SERVERS =
			Collections.newSetFromMap(new WeakHashMap<>());

	private FishingLootMode() {
	}

	public static synchronized boolean shouldForceLeatherBoots(MinecraftServer server) {
		return !VANILLA_FISHING_SERVERS.contains(server);
	}

	public static synchronized void setVanillaFishing(MinecraftServer server, boolean vanillaFishing) {
		if (vanillaFishing) {
			VANILLA_FISHING_SERVERS.add(server);
		} else {
			VANILLA_FISHING_SERVERS.remove(server);
		}
	}
}
