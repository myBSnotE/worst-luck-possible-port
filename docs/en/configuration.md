# Configuration

## Requirements and entry points

Worst Luck Possible uses Fabric API for key bindings, lifecycle events, and authenticated client/server configuration packets. [Mod Menu](https://modrinth.com/mod/modmenu) is optional.

The settings screen can be opened in two ways:

1. Select **Worst Luck Possible → Configure** in Mod Menu.
2. Press the configurable **Open Worst Luck Possible settings** key, bound to `L` by default.

Every control has a hover tooltip. Tooltips describe the selected value, not merely the option name. English and Russian translations are included.

The screen has **General**, **Combat & explosions**, **Spawning**, and **Loot** pages. Switching pages does not save early; the **Save** button submits the complete configuration.

## Profiles

| Profile | Behavior |
| --- | --- |
| Maximum bad luck | Applies the mod-controlled worst value to every configurable gameplay mechanic. |
| Vanilla | Disables every configurable gameplay intervention. |
| Custom | Appears automatically when individual values no longer match either complete preset. |

Profiles deliberately do not change Responsible mode because that option controls permissions rather than gameplay.

## Default settings and world settings

There are two independent configuration layers.

### Defaults

The file `config/worst-luck-possible.json` contains defaults for worlds that have not yet received a configuration. Opening the screen outside a world edits this file.

Changing defaults does not rewrite existing worlds.

### Per-world configuration

On the first start of a world with this version, the current defaults are copied to:

`<world>/worst-luck-possible.json`

After that, the world file is authoritative. Opening the settings screen while connected to a world displays the server's copy rather than the client's defaults.

The server validates every update, writes it atomically through a temporary file, normalizes invalid percentages, and broadcasts the accepted configuration to connected players.

## Permissions

- A dedicated server accepts changes only from players satisfying Minecraft's game-master/operator permission check.
- In an ordinary single-player world, the world owner may edit settings even without commands enabled.
- Other clients receive a read-only screen.
- Editing the JSON file directly while the server is stopped is outside the game's permission model and cannot be prevented by a mod.

## Responsible mode

Responsible mode is intended for challenge worlds.

1. Enable it in the defaults before creating or first opening a world.
2. The new world copies both the gameplay settings and the responsible-mode flag.
3. The single-player owner can no longer change that world's settings without operator permissions.
4. Enabling commands/cheats or otherwise receiving game-master permissions allows changes.
5. Disabling responsible mode itself requires the same permission and a confirmation dialog.

Changing the global defaults cannot unlock an existing responsible-mode world.

## Current settings

### Thunderstorm frequency

| Value | Behavior |
| --- | --- |
| Maximum | When the world is clear, rain and thunder countdowns are capped at 12,000 ticks (ten minutes). |
| Vanilla | Weather countdowns are not modified. |

### Lightning targets

| Value | Behavior |
| --- | --- |
| Loaded area | A successful mod-controlled attempt uses a vanilla surface position in the ticking chunk. |
| Players and passive mobs | Rain-exposed players are preferred, followed by passive mobs. No target means no strike. |
| Players, passive mobs, and flammable blocks | Uses the previous priorities, then samples rain-exposed burnable full blocks with a solid upper face. Thin or multipart blocks such as doors, trapdoors, signs, and beds are excluded. No target means no strike. |
| Vanilla | The injection returns without cancelling Minecraft's original `tickThunder` implementation. |

### Lightning frequency

The slider is a 1–100% chance applied to every mod-controlled lightning opportunity. `100%` preserves the full-intensity behavior from earlier releases. It has no effect when lightning targets are set to Vanilla.

### Fishing

| Value | Initial bite wait | Catch loot |
| --- | --- | --- |
| Vanilla | Vanilla 100–600 tick roll | Vanilla loot table |
| Bad | 600 ticks | One fully damaged pair of leather boots |
| Long vanilla | 600 ticks | Vanilla loot table |

Rain, sky access, Lure, and later fishing phases remain vanilla in all three modes.

### Smart burning

| Value | Forced spread | Fire aging and fuel | Lava ignition |
| --- | --- | --- | --- |
| Off | Vanilla | Vanilla | Vanilla |
| Eternal | Up to eight extra vanilla-valid ignitions | Fueled fire is held at age 0; direct random fuel consumption is deferred until extinguishing | Up to eight extra valid ignitions |
| Accelerated | 2.3.1-style forced direct-neighbor pass, then extended valid air positions; eight placements total | Vanilla aging and direct fuel consumption | Up to eight extra valid ignitions |

All modes obey Minecraft 1.21.11's `fire_spread_radius_around_player` world rule (`-1` allows fire spread everywhere). In Eternal and Accelerated modes, a new air-fire position additionally requires a flammable block directly below it; the original vanilla fire and lava writes are constrained during those enhanced ticks as well. Off keeps vanilla placement rules.

### Player projectile spread

| Value | Behavior |
| --- | --- |
| Vanilla | Keeps Minecraft's random projectile uncertainty. |
| Worst spread | Chooses a varying point on the boundary of the same per-axis uncertainty cube without changing projectile speed. |

### Hostile projectile aim

| Value | Behavior |
| --- | --- |
| Vanilla | Keeps the hostile shooter's ordinary random spread. |
| Lead players | Uses only the vanilla uncertainty allowance to approach the player's predicted position. If an exact intercept is outside that allowance, the closest permitted direction is used. |

### Critical projectile damage

| Value | Behavior |
| --- | --- |
| Vanilla | Keeps the vanilla random critical bonus. |
| Worst outcome | Minimizes the bonus for player-owned persistent projectiles and maximizes it for hostile-owned projectiles. |

### Explosions

| Value | Behavior |
| --- | --- |
| Vanilla | Keeps random explosion-ray strength, fire rolls, and explosion-dependent block-drop rolls unchanged. |
| Maximum destruction | Uses maximum vanilla-random ray strength, succeeds every valid fire roll for explosions that already create fire, and fails explosion-dependent drops whenever vanilla permits failure. |

### Mob Looting

The slider selects a fixed effective Looting level for mob `enchanted_count_increase` functions, independently of the attacker's weapon.

| Value | Added count |
| --- | --- |
| Off | 0 |
| Looting I | 1 (default; keeps blaze rods obtainable) |
| Looting II | 2 |
| Looting III | 3 |

`mobLootingLevel` in the JSON accepts any nonnegative integer. Values above 3 are preserved when the screen is opened and saved, unless the slider itself is moved; this supports modpacks with higher Looting levels.

### Natural passive spawning

| Value | Behavior |
| --- | --- |
| Vanilla | Keeps ordinary runtime passive spawning. |
| Disabled | Removes land and water passive groups from ordinary spawn cycles without changing seed-dependent chunk-generation animals. |

### Hostile spawn distance

| Value | Behavior |
| --- | --- |
| Vanilla | Keeps normal distance and vertical checks. |
| 24–32 blocks | Prefers valid positions in that band, while allowing one vanilla-range seed spawn when the nearest player has no hostiles. |

### Hostile spawn intensity

| Value | Behavior |
| --- | --- |
| Vanilla | Keeps pack-size rolls, waves, preliminary attempts, and coordinate jitter. |
| Maximum | Chooses the maximum pack roll, doubles waves and preliminary attempts, and tightens jitter. |

### Distant hostile replacement

When enabled, a full hostile cap may admit a valid close spawn only if a safe distant hostile is removed afterward. The same option controls protection of the 32–128 block hostile reservoir when nearby pressure is low. Disabled restores vanilla cap denial and despawning.

### Phantoms

| Value | Behavior |
| --- | --- |
| Vanilla | Keeps the original cooldown, random chance, and group-size calculation. |
| Always | After three sleepless days, gives each eligible player four phantoms every 1200–1400 ticks when vanilla darkness and spawn-space checks pass. |

## Removed runtime commands

The former `/worstluck fishing ...` and `/worstluck lightning ...` commands have been removed. Their temporary session state has been replaced with persistent settings.

Diagnostic commands remain under `/worstluck debug ...` and still require operator permissions.
