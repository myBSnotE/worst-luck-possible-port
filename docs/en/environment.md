# Weather, fire, lava, and explosions

## Thunderstorm timing

**Vanilla.** Clear-weather, rain, and thunder timers are randomized and can produce long calm periods.

**Mod.** In Maximum mode, clear rain and thunder timers are capped at 12,000 ticks. Vanilla mode performs no modification.

**Result.** Thunderstorms return frequently without forcing the world to remain permanently wet.

## Lightning frequency and targeting

**Vanilla.** A ticking chunk occasionally passes a large random denominator, chooses a random horizontal position, resolves the surface/lightning-rod position, and may create a lightning bolt or skeleton-horse trap.

**Mod.** Non-vanilla targeting replaces the random occurrence with a configurable 1–100% attempt for each thunder opportunity. Depending on the selected mode it chooses the loaded surface, a rain-exposed player, a passive mob, or a sampled exposed burnable surface block. Targeted modes create no bolt when no eligible target exists.

**Result.** Full intensity can make every ticking chunk attempt a strike, while target modes spend successful strikes on vulnerable entities or fire-starting positions rather than empty terrain.

Skeleton-horse traps are created only when a living player is close to the strike, mob spawning is enabled, the strike is not redirected to a lightning rod, and the player's persistent-mob pressure is below 96. The spawned bolt is cosmetic for a trap so the trap can trigger without immediately damaging its horse.

## Fire placement bug fix

**Vanilla.** Fire may exist over a solid nonflammable top surface, but extended spread only considers an air position when a neighboring block gives that position a positive burn chance.

**Earlier mod behavior.** The player/passive-mob priority path could create fire at an entity's feet on a solid nonflammable floor without checking adjacent fuel.

**Current result.** Entity targeting now also requires `getBurnChance(world, target) > 0`. Fire may be placed over stone when nearby fuel makes the exact position vanilla-valid, but it cannot bridge across a completely nonflammable floor.

## Accelerated fire spread

**Vanilla.** A scheduled fire tick directly tries six adjacent blocks, then evaluates the extended x/z ±1 and y −1…4 volume. Every candidate is probabilistic and depends on neighboring blocks' burn/spread values, age, rain, difficulty, and dimension attributes.

**Mod.** Accelerated mode restores the aggressive 2.3.1 post-tick pass: after vanilla finishes, every still-flammable direct neighbor is forced through first, then valid air positions in the same extended volume are filled, with one shared budget of eight successful placements. Eternal mode uses its own pre-tick prioritized scan. In both modes, entity-foot and air targets must pass vanilla's neighboring-fuel test.

**Result.** Accelerated fire once again has the visibly immediate 2.3.1 behavior while keeping vanilla aging and fuel consumption. It cannot create fire across a completely nonflammable surface, and the per-source budget prevents an unbounded block scan or write loop.

## Eternal fueled fire

**Vanilla.** Fire age rises to 15. Direct spread rolls may replace or remove neighboring fuel, and unsupported old fire eventually disappears.

**Mod.** Eternal mode clamps a fueled source's stored age to 0 and redirects vanilla direct-burn calls so fuel remains present. When ordinary fire is explicitly extinguished, all six adjacent flammable blocks are consumed in the same tick; TNT is primed before removal.

**Result.** A fueled fire persists until intervention, while extinguishing it causes the deferred worst outcome. Accelerated mode deliberately does not apply this behavior.

## Lava ignition

**Vanilla.** A random-ticking lava block takes one of two random branches and checks a small reachable volume for air with a burnable neighbor.

**Mod.** Accelerated and Eternal modes enumerate positions reachable by the one- and two-step vanilla branches, prioritize valid positions over solid nonflammable floors, and ignite up to eight randomly ordered valid candidates.

**Result.** A lava random tick is far more likely to start all useful nearby fires, without creating fire where no burnable neighbor exists.

## Explosion ray strength

**Vanilla.** Every explosion ray starts with `power × (0.7 + random.nextFloat() × 0.6)`.

**Mod.** The runtime float is replaced with the greatest representable value below 1.

**Result.** Every ray starts at almost `1.3 × power`, the maximum result reachable by the vanilla formula.

## Explosion fire

**Vanilla.** An explosion created with `createFire=true` tries each destroyed position with a one-in-three random roll, then checks air and a solid block below.

**Mod.** That existing roll always succeeds. The mod does not turn `createFire` on for explosions that were created without it.

**Result.** Fire-capable explosions ignite every geometrically valid affected position; ordinary non-fire explosions remain non-fire explosions.

## Explosion drops

**Vanilla.** `survives_explosion` and `explosion_decay` compare a random float with `1 / explosionRadius`. Some blocks or loot paths do not use these functions and are guaranteed.

**Mod.** The compared float becomes the greatest value below 1.

**Result.** A drop fails whenever the vanilla predicate permits failure. Radius 1 or another guaranteed case remains guaranteed because the comparison still succeeds at a threshold of 1.
