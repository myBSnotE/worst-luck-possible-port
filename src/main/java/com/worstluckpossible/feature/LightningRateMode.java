package com.worstluckpossible.feature;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import net.minecraft.server.MinecraftServer;

/**
 * Holds the temporary reduced-lightning mode for each running server instance.
 * A newly opened world or restarted dedicated server returns to full intensity.
 */
public final class LightningRateMode {
	private static final Set<MinecraftServer> REDUCED_LIGHTNING_SERVERS =
			Collections.newSetFromMap(new WeakHashMap<>());

	private LightningRateMode() {
	}

	public static synchronized boolean isReduced(MinecraftServer server) {
		return REDUCED_LIGHTNING_SERVERS.contains(server);
	}

	public static synchronized void setReduced(MinecraftServer server, boolean reduced) {
		if (reduced) {
			REDUCED_LIGHTNING_SERVERS.add(server);
		} else {
			REDUCED_LIGHTNING_SERVERS.remove(server);
		}
	}
}
