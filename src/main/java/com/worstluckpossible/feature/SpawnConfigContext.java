package com.worstluckpossible.feature;

import com.worstluckpossible.config.WorstLuckConfig;

/**
 * Carries the active world's spawn settings through vanilla helper callbacks
 * that do not receive a world parameter of their own.
 */
public final class SpawnConfigContext {
	private static final ThreadLocal<WorstLuckConfig> ACTIVE = new ThreadLocal<>();

	private SpawnConfigContext() {}

	public static void enter(WorstLuckConfig config) {
		ACTIVE.set(config);
	}

	public static void exit() {
		ACTIVE.remove();
	}

	public static WorstLuckConfig current() {
		return ACTIVE.get();
	}
}