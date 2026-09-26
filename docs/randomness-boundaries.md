# Randomness boundaries and safeguards

## Core policy

Worst Luck Possible changes **runtime random values** when Minecraft is about to choose between a favorable and unfavorable supported result.

The mod deliberately does not reinterpret outcomes that have already been fixed by a seed or previously generated state.

## Included

Typical included calls are:

- `Random.nextInt`, `nextFloat`, and triangular offsets used during a live action;
- runtime mob equipment, size, rider, potion, drop, weather, explosion, and projectile rolls;
- loot number/chance evaluation performed when loot is generated;
- candidate ordering when all candidates are still validated by vanilla rules.

## Excluded

### Enchanting-table offers

The table uses the stored `enchantingTableSeed` and derives displayed offers from it. Those offers are already seed-determined before the player selects one, so the mod does not rewrite them.

### Structure and container generation seeds

Structure chests and similar containers may supply inventory from an explicit saved/generated long seed. The mod does not replace that seed or retroactively alter inventories already generated from it.

### World generation

Biome layout, structures, ore placement, and chunk-generation animal populations are not converted into different results merely because a different world seed would have been worse.

### Deterministic formulas

Armor distribution, ordinary melee damage, shield cooldowns, and effects that have no random branch are not changed under the banner of “bad luck.” A deterministic result is not a luck roll.

## Vanilla-validity rule

Selecting the worst random result does not normally bypass the nonrandom validation around it.

Examples:

- accelerated fire still requires a positive vanilla neighboring-fuel burn chance;
- lava ignition still requires air and a burnable neighbor;
- hostile projectile leading remains inside the vanilla uncertainty cube;
- chorus destinations still pass vanilla collision and border checks;
- targeted lightning requires rain exposure;
- natural spawns still pass pool, placement, collision, light, difficulty, and game-rule checks;
- fire-capable explosion behavior does not turn `createFire` on for another explosion type.

## Existing-state safety

- Stored villager offers are not rewritten; only newly generated offers change.
- Picked-up foreign mob equipment retains ownership/drop guarantees.
- Existing and manually summoned wandering traders are not deleted.
- Named, persistent, raid, and protected mobs are excluded from mob-cap replacement.
- A distant hostile is removed only after a valid closer replacement has spawned.

## Performance safeguards

- Fire and lava add at most eight ignitions per source opportunity.
- Flammable lightning targeting uses a bounded surface search.
- Spawn pressure data is cached rather than rescanned for every individual decision.
- Replacement and pathfinding use adaptive tick budgets.
- Skeleton-horse traps stop being created around a player after persistent-mob pressure reaches the safety threshold.
- Target prediction is calculated once at projectile launch; there is no per-tick homing calculation.

## Configuration authority

The server owns world settings. Client packets contain a proposed normalized configuration, and the server rejects them unless the sender has permission under the rules described in [Configuration](configuration.md#permissions). The accepted state is then broadcast back to players.
