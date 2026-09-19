package com.worstluckpossible;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorstLuckPossible implements ModInitializer {
	public static final String MOD_ID = "worst-luck-possible";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Worst luck possible: good luck disabled.");
	}
}
