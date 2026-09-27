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
	public enum PassiveSpawnMode { VANILLA, DISABLED }
	public enum HostileSpawnDistance { VANILLA, CLOSE }
	public enum HostilePackMode { VANILLA, MAXIMUM }
	public enum PhantomMode { VANILLA, FOUR }

	public int formatVersion = 3;
	public StormFrequency stormFrequency = StormFrequency.MAXIMUM;
	public LightningTargets lightningTargets = LightningTargets.LOADED_AREA;
	public int lightningFrequencyPercent = 100;
	public FishingMode fishingMode = FishingMode.BAD;
	public FireMode fireMode = FireMode.ETERNAL;
	public PlayerProjectileSpread playerProjectileSpread = PlayerProjectileSpread.WORST;
	public HostileProjectileAim hostileProjectileAim = HostileProjectileAim.LEADING;
	public ProjectileCriticalDamage projectileCriticalDamage = ProjectileCriticalDamage.WORST;
	public ExplosionMode explosionMode = ExplosionMode.MAXIMUM;
	/**
	 * Fixed effective Looting bonus applied by mob loot functions.
	 * The screen exposes 0..3, while config files intentionally accept any value >= 0.
	 */
	public int mobLootingLevel = 1;
	public PassiveSpawnMode passiveSpawnMode = PassiveSpawnMode.DISABLED;
	public HostileSpawnDistance hostileSpawnDistance = HostileSpawnDistance.CLOSE;
	public HostilePackMode hostilePackMode = HostilePackMode.MAXIMUM;
	public boolean distantHostileReplacement = true;
	public PhantomMode phantomMode = PhantomMode.FOUR;
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
		copy.mobLootingLevel = mobLootingLevel;
		copy.passiveSpawnMode = passiveSpawnMode;
		copy.hostileSpawnDistance = hostileSpawnDistance;
		copy.hostilePackMode = hostilePackMode;
		copy.distantHostileReplacement = distantHostileReplacement;
		copy.phantomMode = phantomMode;
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
		if (passiveSpawnMode == null) passiveSpawnMode = PassiveSpawnMode.DISABLED;
		if (hostileSpawnDistance == null) hostileSpawnDistance = HostileSpawnDistance.CLOSE;
		if (hostilePackMode == null) hostilePackMode = HostilePackMode.MAXIMUM;
		if (phantomMode == null) phantomMode = PhantomMode.FOUR;
		lightningFrequencyPercent = Math.max(1, Math.min(100, lightningFrequencyPercent));
		mobLootingLevel = Math.max(0, mobLootingLevel);
		formatVersion = 3;
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
		mobLootingLevel = 0;
		passiveSpawnMode = PassiveSpawnMode.VANILLA;
		hostileSpawnDistance = HostileSpawnDistance.VANILLA;
		hostilePackMode = HostilePackMode.VANILLA;
		distantHostileReplacement = false;
		phantomMode = PhantomMode.VANILLA;
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
		mobLootingLevel = 1;
		passiveSpawnMode = PassiveSpawnMode.DISABLED;
		hostileSpawnDistance = HostileSpawnDistance.CLOSE;
		hostilePackMode = HostilePackMode.MAXIMUM;
		distantHostileReplacement = true;
		phantomMode = PhantomMode.FOUR;
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
				&& explosionMode == ExplosionMode.VANILLA
				&& mobLootingLevel == 0
				&& passiveSpawnMode == PassiveSpawnMode.VANILLA
				&& hostileSpawnDistance == HostileSpawnDistance.VANILLA
				&& hostilePackMode == HostilePackMode.VANILLA
				&& !distantHostileReplacement
				&& phantomMode == PhantomMode.VANILLA;
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
				&& explosionMode == ExplosionMode.MAXIMUM
				&& mobLootingLevel == 1
				&& passiveSpawnMode == PassiveSpawnMode.DISABLED
				&& hostileSpawnDistance == HostileSpawnDistance.CLOSE
				&& hostilePackMode == HostilePackMode.MAXIMUM
				&& distantHostileReplacement
				&& phantomMode == PhantomMode.FOUR;
	}
}
