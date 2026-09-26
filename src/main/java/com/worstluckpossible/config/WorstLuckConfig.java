package com.worstluckpossible.config;

/** Serializable gameplay settings. A world receives its own copy of the defaults. */
public final class WorstLuckConfig {
	public enum StormFrequency { MAXIMUM, VANILLA }
	public enum LightningTargets { LOADED_AREA, PLAYERS_AND_PASSIVES, PLAYERS_PASSIVES_AND_FLAMMABLES, VANILLA }
	public enum FishingMode { VANILLA, BAD, LONG_VANILLA }
	public enum FireMode { VANILLA, ETERNAL, ACCELERATED }
	public enum PlayerProjectileSpread { VANILLA, WORST }
	public enum HostileProjectileAim { VANILLA, LEADING }
	public enum ProjectileCriticalDamage { VANILLA, WORST }
	public enum ExplosionMode { VANILLA, MAXIMUM }

	public int formatVersion = 2;
	public StormFrequency stormFrequency = StormFrequency.MAXIMUM;
	public LightningTargets lightningTargets = LightningTargets.LOADED_AREA;
	public int lightningFrequencyPercent = 100;
	public FishingMode fishingMode = FishingMode.BAD;
	public FireMode fireMode = FireMode.ETERNAL;
	public PlayerProjectileSpread playerProjectileSpread = PlayerProjectileSpread.WORST;
	public HostileProjectileAim hostileProjectileAim = HostileProjectileAim.LEADING;
	public ProjectileCriticalDamage projectileCriticalDamage = ProjectileCriticalDamage.WORST;
	public ExplosionMode explosionMode = ExplosionMode.MAXIMUM;
	public boolean responsibleMode = false;

	public WorstLuckConfig copy() {
		WorstLuckConfig copy = new WorstLuckConfig();
		copy.formatVersion = formatVersion;
		copy.stormFrequency = stormFrequency;
		copy.lightningTargets = lightningTargets;
		copy.lightningFrequencyPercent = lightningFrequencyPercent;
		copy.fishingMode = fishingMode;
		copy.fireMode = fireMode;
		copy.playerProjectileSpread = playerProjectileSpread;
		copy.hostileProjectileAim = hostileProjectileAim;
		copy.projectileCriticalDamage = projectileCriticalDamage;
		copy.explosionMode = explosionMode;
		copy.responsibleMode = responsibleMode;
		return copy;
	}

	public WorstLuckConfig normalize() {
		if (stormFrequency == null) stormFrequency = StormFrequency.MAXIMUM;
		if (lightningTargets == null) lightningTargets = LightningTargets.LOADED_AREA;
		if (fishingMode == null) fishingMode = FishingMode.BAD;
		if (fireMode == null) fireMode = FireMode.ETERNAL;
		if (playerProjectileSpread == null) playerProjectileSpread = PlayerProjectileSpread.WORST;
		if (hostileProjectileAim == null) hostileProjectileAim = HostileProjectileAim.LEADING;
		if (projectileCriticalDamage == null) projectileCriticalDamage = ProjectileCriticalDamage.WORST;
		if (explosionMode == null) explosionMode = ExplosionMode.MAXIMUM;
		lightningFrequencyPercent = Math.max(1, Math.min(100, lightningFrequencyPercent));
		formatVersion = 2;
		return this;
	}

	public void applyVanillaPreset() {
		stormFrequency = StormFrequency.VANILLA;
		lightningTargets = LightningTargets.VANILLA;
		lightningFrequencyPercent = 100;
		fishingMode = FishingMode.VANILLA;
		fireMode = FireMode.VANILLA;
		playerProjectileSpread = PlayerProjectileSpread.VANILLA;
		hostileProjectileAim = HostileProjectileAim.VANILLA;
		projectileCriticalDamage = ProjectileCriticalDamage.VANILLA;
		explosionMode = ExplosionMode.VANILLA;
	}

	public void applyWorstPreset() {
		stormFrequency = StormFrequency.MAXIMUM;
		lightningTargets = LightningTargets.LOADED_AREA;
		lightningFrequencyPercent = 100;
		fishingMode = FishingMode.BAD;
		fireMode = FireMode.ETERNAL;
		playerProjectileSpread = PlayerProjectileSpread.WORST;
		hostileProjectileAim = HostileProjectileAim.LEADING;
		projectileCriticalDamage = ProjectileCriticalDamage.WORST;
		explosionMode = ExplosionMode.MAXIMUM;
	}

	public boolean isVanillaPreset() {
		return stormFrequency == StormFrequency.VANILLA
				&& lightningTargets == LightningTargets.VANILLA
				&& lightningFrequencyPercent == 100
				&& fishingMode == FishingMode.VANILLA
				&& fireMode == FireMode.VANILLA
				&& playerProjectileSpread == PlayerProjectileSpread.VANILLA
				&& hostileProjectileAim == HostileProjectileAim.VANILLA
				&& projectileCriticalDamage == ProjectileCriticalDamage.VANILLA
				&& explosionMode == ExplosionMode.VANILLA;
	}

	public boolean isWorstPreset() {
		return stormFrequency == StormFrequency.MAXIMUM
				&& lightningTargets == LightningTargets.LOADED_AREA
				&& lightningFrequencyPercent == 100
				&& fishingMode == FishingMode.BAD
				&& fireMode == FireMode.ETERNAL
				&& playerProjectileSpread == PlayerProjectileSpread.WORST
				&& hostileProjectileAim == HostileProjectileAim.LEADING
				&& projectileCriticalDamage == ProjectileCriticalDamage.WORST
				&& explosionMode == ExplosionMode.MAXIMUM;
	}
}
