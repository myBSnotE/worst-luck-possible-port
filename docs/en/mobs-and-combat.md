# Mobs, equipment, projectiles, movement, and bosses

## Hostile approach behavior

**Vanilla.** Idle hostile mobs may wander without deliberately closing on the nearest player unless another target goal takes control.

**Mod.** Hostile mobs receive a priority-4 goal that approaches the nearest eligible player.

**Result.** Idle movement tends to reduce distance to players while higher-priority combat and safety goals can still override it.

## Dropped-item impulse

**Vanilla.** Newly constructed item entities receive a small random initial velocity.

**Mod.** That constructor impulse is oriented generally away from the nearest player.

**Result.** Natural drops are less convenient to collect. Explicit throws from players, dispensers, and systems that overwrite constructor velocity retain their later vanilla direction.

## Zombies

| Mechanic | Vanilla | Mod and result |
| --- | --- | --- |
| Baby state | Random subset of spawned zombies | Eligible zombies and variants choose baby state. |
| Leader roll | Rare random leader attributes | Eligible zombies choose the leader outcome. |
| Weapon | Equipment rolls may leave the hand empty or choose weaker gear | Supported zombies receive an iron sword through the worst equipment path. |
| Door breaking | Restricted by difficulty and goals | On Hard, eligible zombies retain the door-breaking behavior. |
| Reinforcements | Damaged leaders roll to summon helpers | The unfavorable roll is selected until local safety limits prevent runaway growth. |

## Zombie-villager curing

**Vanilla.** Conversion starts within a random range and nearby beds or iron bars can randomly accelerate individual ticks.

**Mod.** The timer starts at 6,000 ticks and random environmental acceleration does not occur.

**Result.** Curing takes the maximum base time even in a prepared room.

## Drowned

**Vanilla.** Drowned are one possible water spawn and only a subset receives a trident. A submerged living passenger normally dismounts from vehicles such as chickens.

**Mod.** Water spawn selection favors drowned where the vanilla pool permits them, eligible drowned receive tridents, and drowned chicken jockeys remain mounted underwater.

**Result.** Water encounters more often contain armed drowned, including functional chicken-mounted variants that can travel toward the surface.

## Spiders

**Vanilla.** A small fraction of spiders spawn with a skeleton rider, and Hard difficulty may give a random permanent status effect.

**Mod.** Eligible spiders choose the jockey outcome. On Hard, the random effect becomes permanent Speed I.

**Result.** Spiders consistently carry ranged support and receive the mobility effect most useful for closing distance.

## Slimes and magma cubes

**Vanilla.** Natural size is randomly selected within each spawn path. Slime jump delay is random; magma cubes multiply the chosen delay by four.

**Mod.** Natural spawns choose the largest natural size. Slimes choose the minimum jump delay, while magma cubes keep their vanilla ×4 relationship.

**Result.** The initial mob is maximally large and ordinary slimes attack as frequently as the vanilla delay range allows.

## Shulkers

**Vanilla.** The delay between bullet attempts contains a random interval.

**Mod.** The interval is set to its minimum vanilla value.

**Result.** Shulkers attempt to fire as rapidly as their existing state machine permits.

## Witches

**Vanilla.** Self-buff and attack-potion selection contains ordered conditions with random failures, allowing harmless or less useful choices.

**Mod.** Favorable failures are removed while preserving the normal condition order and applicability checks.

**Result.** Witches select the most harmful currently applicable potion rather than throwing an impossible potion or ignoring vanilla state conditions.

## Endermen

**Vanilla.** Random teleport coordinates are sampled around the enderman.

**Mod.** Runtime offsets are redirected 10–50 blocks along the targeted player's look direction, then passed through vanilla destination validation.

**Result.** Teleports tend to place the enderman where the player is looking or moving attention, but invalid blocks, collisions, and unloaded destinations are still rejected.

## Mob equipment

### Equipment quality

**Vanilla.** Local difficulty and random rolls decide whether equipment exists and which material tier is used.

**Mod.** Eligible random equipment rolls select the maximum supported vanilla result rather than hard-coding one armor set for every mob.

**Result.** Mobs receive strong gear appropriate to the original equipment path.

### Enchantments

**Vanilla.** Equipment enchantment level and generated enchantments are random and constrained by item compatibility.

**Mod.** Eligible mob gear receives maximum combat-oriented, item-valid enchantments.

**Result.** Armor and weapons are dangerous without creating combinations that the item cannot support.

### Durability while worn

**Vanilla.** Mob armor loses durability when absorbing damage, and helmets can be damaged while protecting undead from sunlight.

**Mod.** Equipped mob armor does not lose combat durability; sunlight does not consume helmet durability.

**Result.** Hostile protection does not conveniently break during combat. If the stack later becomes a normal dropped/player-owned item, it is not globally made unbreakable.

### Equipment drops

**Vanilla.** Naturally equipped items have drop chances; items deliberately picked up from the ground can be guaranteed.

**Mod.** Naturally generated equipment chooses the no-drop result. Foreign/picked-up items keep vanilla ownership and guaranteed-return behavior.

**Result.** Powerful generated gear cannot be farmed, while the player does not lose an item merely because a mob picked it up.

## Player projectile spread

**Vanilla.** Projectile launch direction adds independent triangular random offsets to x, y, and z inside an uncertainty cube, then normalizes and applies speed.

**Mod.** Player-owned projectiles choose a random azimuth perpendicular to the aim direction and extend the error to the boundary of the same per-axis uncertainty cube.

**Result.** The shot receives maximum permitted angular inconvenience without altering speed. Random azimuth avoids all stationary-camera shots converging on one deterministic point.

## Hostile projectile leading

**Vanilla.** Skeletons, drowned, and similar shooters aim at the target's current position, then receive random uncertainty.

**Mod.** For hostile-owned projectiles targeting a living player, the server's accepted movement vector is projected over estimated flight time with vanilla 0.99 drag. The desired intercept is clamped to the set of directions reachable inside the existing uncertainty cube. If exact interception is impossible, a sampled closest permitted direction is used.

**Result.** Vanilla spread is spent to lead a moving player instead of missing randomly. Speed, gravity, drag, and in-flight direction are unchanged; this is not homing. Dispenser spread is not modified by this branch.

## Critical projectile damage

**Vanilla.** A critical persistent projectile adds `random.nextInt(baseDamage / 2 + 2)`.

**Mod.** Player-owned critical projectiles receive zero bonus. Hostile-mob-owned projectiles receive `bound − 1`, the maximum. Other owners retain the vanilla call.

**Result.** Player critical arrows lose their random upside, while hostile critical arrows gain the largest legal bonus.

## Ender Dragon

**Vanilla.** The dragon can transition into landing approach, sitting/perching, takeoff, holding, strafing, and circling phases with variable path heights.

**Mod.** Landing and sitting transitions become circling, while strafe and player-approach paths use their maximum unfavorable flight height.

**Result.** The dragon does not offer normal perch windows and remains harder to reach during affected flight phases.
