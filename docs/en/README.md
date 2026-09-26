# Worst Luck Possible documentation

This documentation explains the behavior of the Minecraft 1.21.11 port. Each section separates:

- **Vanilla** — the original runtime rule or random roll.
- **Mod** — the value or decision changed by Worst Luck Possible.
- **Result** — the behavior visible to a player.

The mod's central rule is documented in [Randomness boundaries](randomness-boundaries.md): runtime randomness may be made unfavorable, while outcomes already fixed by a world or table seed are left unchanged.

## Contents

- [Configuration, Mod Menu, permissions, and responsible mode](configuration.md)
- [Weather, lightning, fire, lava, and explosions](environment.md)
- [Loot, fishing, items, progression, and trading](loot-and-progression.md)
- [Mobs, equipment, projectiles, movement, and bosses](mobs-and-combat.md)
- [Natural spawning, despawning, phantoms, and raids](spawning-and-raids.md)
- [Randomness boundaries and implementation safeguards](randomness-boundaries.md)

The root [README](../../README.md) remains the short installation and gameplay overview. Release-specific changes are stored in [`release-notes/`](../../release-notes/).
