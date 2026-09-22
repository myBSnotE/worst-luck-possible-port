# Worst Luck Possible — Minecraft 1.21.11 port

A modern Fabric port of **Worst Luck Possible**, a challenge mod that removes favorable randomness and consistently gives the player the worst practical outcome.

This repository ports the original Minecraft 1.16.1 mod to **Minecraft 1.21.11** while preserving its gameplay intent and adapting the implementation to current Minecraft internals.

## Original mod

- **Author:** Heppe
- **Original source link declared by the 1.16.1 mod:** [FabricMC/worst-luck-possible](https://github.com/FabricMC/worst-luck-possible)
- **Author homepage:** [HeppeGaming](https://youtube.com/c/HeppeGaming)
- **Original version:** 1.0.0 for Minecraft 1.16.1
- **License declared by the original mod:** CC0-1.0

The original compiled JAR used as the behavioral reference is included at [`worst-luck-possible-1.0.0.jar`](worst-luck-possible-1.0.0.jar).

## Requirements

- Minecraft **1.21.11**
- Fabric Loader **0.19.5** or newer compatible version
- Java **21**
- Fabric API is **not required**

## Installation

1. Install Fabric Loader for Minecraft 1.21.11.
2. Download [`dist/worst-luck-possible-2.0.0.jar`](dist/worst-luck-possible-2.0.0.jar).
3. Put the JAR into the instance or server `mods` directory.
4. Start the game or server.

The mod works on dedicated servers and supports multiple players.

## Original gameplay features

### Loot

- Mob and block loot rolls choose the smallest possible result.
- Gravel never drops flint.
- Piglin bartering always gives two magma cream.
- Freshly dropped items fly away from the nearest player.
- Equipment drops are suppressed.

### Hostile mobs

- Zombies and zombie variants are always babies.
- Zombies receive difficulty-aware equipment and weapons.
- Drowned always carry tridents.
- Every spider becomes a spider jockey; on Hard the rider is invisible.
- Zombie leaders reliably call reinforcements when damaged on Hard.
- Endermen teleport along the direction the player is looking.
- The Ender Dragon does not perch and keeps an unfavorable flight height.
- Hostile mobs actively path toward nearby players.

### Natural spawning

- Passive mobs do not spawn naturally during ordinary mob-spawn cycles.
- Fish and dolphins are disabled.
- Hostile mobs spawn in the largest practical packs with additional spawn attempts.
- Natural hostile spawns are kept close to a valid nearby player while retaining normal block, light, biome and collision checks.
- Drowned are made more common through hostile spawn substitution.
- Distant hostile mobs no longer permanently occupy the mob cap.

### Other

- Projectiles receive strongly randomized inaccuracy.
- Thunderstorms begin frequently, with lightning attempts across loaded chunks.
- The fixed-world-seed feature from the original mod is intentionally omitted.

## Additions and changes in this port

### Adaptive hostile mob-cap replacement

When the hostile mob cap is full, the mod may temporarily allow a valid close spawn. Only after that spawn succeeds does it remove the farthest eligible hostile between 32 and 128 blocks from its nearest player.

- Nothing is removed if a valid closer replacement cannot spawn.
- Named, persistent and otherwise protected mobs are never selected.
- Distance is always measured to each mob's **nearest player**, so multiplayer users cannot cause mobs to disappear in front of one another.
- Replacement work is limited to four mobs per tick to avoid excessive server load.
- Ordinary immediate despawning remains at 128 blocks; mobs around farms or platforms 64 blocks away are not forcibly deleted.

### Guaranteed phantom attacks

After a player has avoided sleep for three in-game days, phantom attacks are guaranteed every **1200–1400 ticks** when vanilla-compatible conditions are satisfied:

- the player is at Y 64 or higher;
- the sky is visible above the player;
- it is sufficiently dark for the vanilla phantom spawner;
- phantom spawning and hostile spawning are enabled;
- the player is not a spectator;
- the selected spawn position is clear.

Each eligible player receives a pack of **four phantoms**, spawning **20–34 blocks above** with up to **9 blocks of horizontal offset**. Eligibility is evaluated independently for every player.

### Modernized implementation

- Ported mixins and mappings to Minecraft 1.21.11/Yarn.
- Updated to Java 21 and the current Fabric toolchain.
- Added multiplayer-safe nearest-player distance handling.
- Added automated GitHub Actions builds and dedicated-server smoke tests.

## Building

The repository uses Gradle and Fabric Loom. CI builds the mod and writes the remapped artifact to:

```text
dist/worst-luck-possible-2.0.0.jar
```

The workflow also launches a temporary Fabric dedicated server to catch mixin application failures and startup crashes.

## Disclaimer

This is an unofficial modernization of the original mod. Minecraft is a trademark of Mojang Studios. This project is not affiliated with or endorsed by Mojang Studios.
