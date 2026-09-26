# Configuration

## Requirements and entry points

Worst Luck Possible uses Fabric API for key bindings, lifecycle events, and authenticated client/server configuration packets. [Mod Menu](https://modrinth.com/mod/modmenu) is optional.

The settings screen can be opened in two ways:

1. Select **Worst Luck Possible → Configure** in Mod Menu.
2. Press the configurable **Open Worst Luck Possible settings** key, bound to `L` by default.

Every control has a hover tooltip. Tooltips describe the selected value, not merely the option name. English and Russian translations are included.

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
| Players, passive mobs, and flammable blocks | Uses the previous priorities, then samples rain-exposed surface positions for a burnable block. No target means no strike. |
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
| Accelerated | Up to eight extra vanilla-valid ignitions | Vanilla aging and direct fuel consumption | Up to eight extra valid ignitions |

All modes obey the `doFireTick`/fire-spread world rule. The accelerated algorithms require the same nearby burnable support that vanilla checks; they cannot cross a completely nonflammable surface by themselves.

## Removed runtime commands

The former `/worstluck fishing ...` and `/worstluck lightning ...` commands have been removed. Their temporary session state has been replaced with persistent settings.

Diagnostic commands remain under `/worstluck debug ...` and still require operator permissions.
