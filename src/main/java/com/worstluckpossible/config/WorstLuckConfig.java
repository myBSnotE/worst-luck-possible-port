package com.worstluckpossible.config;

/** Serializable gameplay settings. A world receives its own copy of the defaults. */
public final class WorstLuckConfig {
	public enum StormFrequency { MAXIMUM, VANILLA }
	public enum LightningTargets { LOADED_AREA, PLAYERS_AND_PASSIVES, PLAYERS_PASSIVES_AND_FLAMMABLES, VANILLA }
	public enum FishingMode { VANILLA, BAD, LONG_VANILLA }
	public enum FireMode { VANILLA, ETERNAL, ACCELERATED }

	public int formatVersion = 1;
	public StormFrequency stormFrequency = StormFrequency.MAXIMUM;
	public LightningTargets lightningTargets = LightningTargets.LOADED_AREA;
	public int lightningFrequencyPercent = 100;
	public FishingMode fishingMode = FishingMode.BAD;
	public FireMode fireMode = FireMode.ETERNAL;
	public boolean responsibleMode = false;

	public WorstLuckConfig copy() {
		WorstLuckConfig copy = new WorstLuckConfig();
		copy.formatVersion = formatVersion;
		copy.stormFrequency = stormFrequency;
		copy.lightningTargets = lightningTargets;
		copy.lightningFrequencyPercent = lightningFrequencyPercent;
		copy.fishingMode = fishingMode;
		copy.fireMode = fireMode;
		copy.responsibleMode = responsibleMode;
		return copy;
	}

	public WorstLuckConfig normalize() {
		if (stormFrequency == null) stormFrequency = StormFrequency.MAXIMUM;
		if (lightningTargets == null) lightningTargets = LightningTargets.LOADED_AREA;
		if (fishingMode == null) fishingMode = FishingMode.BAD;
		if (fireMode == null) fireMode = FireMode.ETERNAL;
		lightningFrequencyPercent = Math.max(1, Math.min(100, lightningFrequencyPercent));
		formatVersion = 1;
		return this;
	}
}
