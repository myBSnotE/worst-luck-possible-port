package com.worstluckpossible;

import com.worstluckpossible.config.WorstLuckConfigManager;
import com.worstluckpossible.network.WorstLuckNetworking;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorstLuckPossible implements ModInitializer {
	public static final String MOD_ID = "worst-luck-possible";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		WorstLuckConfigManager.defaults();
		WorstLuckNetworking.registerPayloads();
		WorstLuckNetworking.registerServerHandlers();
		ServerLifecycleEvents.SERVER_STARTING.register(WorstLuckConfigManager::loadWorld);
		ServerLifecycleEvents.SERVER_STOPPING.register(WorstLuckConfigManager::unload);
		LOGGER.info("Worst luck possible: good luck disabled.");
	}
}
