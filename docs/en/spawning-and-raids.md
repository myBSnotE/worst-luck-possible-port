# Natural spawning, despawning, phantoms, and raids

## Passive natural spawning

**Vanilla.** Regular natural-spawn cycles create land animals, water animals, ambient creatures, and hostile mobs. Some animals are also created during seed-dependent chunk generation.

**Mod.** Runtime natural-spawn cycles reject animal/fish groups. Chunk-generation animals are not modified because those placements are tied to world generation and seed-derived state.

**Result.** Ordinary replenishment of passive mobs stops, but a world's generated population is not retrospectively or seed-independently rewritten.

## Hostile pack size and attempts

**Vanilla.** A natural-spawn cycle uses multiple waves, preliminary attempts, coordinate jitter, and each entry's configured minimum/maximum pack size.

**Mod.** The number of supported waves/attempts is intensified and the configured pack-size roll selects its maximum. Coordinate jitter remains a vanilla runtime roll.

**Result.** Valid hostile groups tend to be as large as their spawn entry allows, without bypassing biome pools, collision, light, placement, or difficulty checks.

## Spawn-pool substitutions

**Drowned.** When an existing water spawn pool permits drowned, selection favors them without inserting drowned into biomes or locations where the pool never contained them.

**Nether harmless mobs.** Supported harmless Nether choices are replaced by the unfavorable neighboring choice, a ghast, while the surrounding spawn validation still runs.

## Close hostile placement

**Vanilla.** Natural spawning searches a broad annulus around players, normally excluding the immediate 24-block safety radius and allowing positions out to the simulation/despawn range.

**Mod.** Ordinary hostile placement prefers valid positions 24–32 blocks from a real player. An empty hostile population permits a limited fallback so a strict close search cannot permanently deadlock the cap.

**Result.** Hostiles appear just outside the vanilla safety radius whenever valid terrain exists.

## Mob-cap replacement

**Vanilla.** Once the hostile cap is full, new natural hostiles do not spawn; existing distant mobs continue occupying the cap until despawned.

**Mod.** The system first proves that a valid closer replacement has spawned, then selects an eligible distant hostile for removal. Named, persistent, raid, protected, and otherwise nonreplaceable mobs are excluded.

**Result.** The cap shifts pressure toward players without deleting an old mob before a replacement actually exists.

## Despawning reservoir

**Vanilla.** Hostiles beyond 128 blocks despawn immediately when checked. Between 32 and 128 blocks, random despawn becomes possible after age conditions.

**Mod.** The immediate >128 rule remains unchanged. In the 32–128 band, eligible hostiles can be temporarily protected when too few enemies are near the nearest assigned player.

**Result.** The world maintains a limited reserve that can move back toward players without making every distant entity permanent.

## Caching and safety budgets

Density snapshots are cached once per second per player. Replacement, candidate scans, and pathfinding use per-tick budgets and emergency limits. A snapshot distinguishes total mobs, persistent mobs, nearby hostiles, distant candidates, and the farthest replaceable hostile.

`/worstluck debug spawning` invalidates and prints the invoking player's current snapshot. It is diagnostic only and requires game-master permissions.

## Phantoms

**Vanilla.** After sufficient insomnia, a periodic spawner rolls whether to create a small phantom group, subject to night, sky, dimension, and game-rule checks.

**Mod.** After three sleepless in-game days, each eligible player receives four phantoms at a guaranteed interval selected from the vanilla 1,200–1,400 tick range. Vanilla night/darkness and eligibility checks remain.

**Result.** Insomnia reliably produces the largest supported attack group instead of frequently rolling no attack.

## Raids

### Wave composition

**Vanilla.** Raid wave counts come from difficulty and wave index; bonus raider counts include random rolls.

**Mod.** Supported bonus rolls choose their vanilla maximum.

**Result.** Waves contain the largest composition their existing difficulty/wave formula allows.

### Witch refills

**Vanilla.** Witches appear in configured waves and counts.

**Mod.** When an active wave falls below the configured pressure threshold, bounded attempts look for a valid dark village position and add a witch. The refill respects active-wave state and does not run during end-of-wave completion in a way that creates unbounded growth.

**Result.** A nearly defeated wave can regain dangerous support, but hard caps and placement checks protect server performance.
