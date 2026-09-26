# Loot, fishing, items, progression, and trading

## General loot rolls

| Mechanic | Vanilla | Mod and result |
| --- | --- | --- |
| Uniform number providers | Choose a value between configured bounds | Choose the minimum bound, so stacks are as small as the table permits. |
| Binomial bonus counts | Perform repeated success/failure trials | Choose failed trials, producing the minimum bonus. |
| Random chance conditions | Pass according to a configured runtime probability | Fail when failure is possible. |
| Enchantment bonus chance | Uses a chance indexed by enchantment level | Chooses the unfavorable runtime outcome without rewriting the enchantment or table. |
| Table bonus conditions | Select a probability from a table such as Fortune flint chances | Chooses failure when the selected vanilla probability allows it; gravel therefore does not yield flint. |

These hooks affect runtime loot evaluation. They do not replace structure-loot seeds or rewrite already generated inventories.

## Fishing

**Vanilla.** The initial waiting phase is uniformly selected from 100–600 ticks before rain, sky access, and Lure adjustments. A successful reel evaluates the fishing loot table.

**Mod.** See [Configuration](configuration.md#fishing). Bad and Long vanilla use the maximum initial 600-tick roll. Only Bad replaces generated loot with one fully damaged leather-boots stack.

**Result.** The user can select completely vanilla fishing, the full bad outcome, or only the long wait.

## Piglin bartering

**Vanilla.** A barter evaluates a weighted loot table with variable count providers.

**Mod.** A successful barter is replaced with exactly two magma cream.

**Result.** Gold cannot produce pearls, gravel, potions, or other strategically valuable barter results.

## Trial spawners

**Vanilla.** Normal and ominous trial spawners select post-combat rewards such as keys, food, and potions.

**Mod.** The ejected reward is one baked potato.

**Result.** Combat still completes normally, but the reward is the least strategically useful supported food choice. Vaults are not changed.

## Eyes of Ender

**Vanilla.** A thrown Eye of Ender has an 80% chance to drop and a 20% chance to shatter.

**Mod.** The runtime survival roll always chooses the shatter branch.

**Result.** Every thrown eye is consumed.

## Ender pearls

**Vanilla.** After a successful player teleport there is a small random chance to spawn an endermite, provided mob-spawning rules allow it.

**Mod.** That chance always succeeds.

**Result.** Every eligible successful player pearl teleport creates an endermite. Failed teleports and disabled monster spawning do not bypass vanilla rules.

## Eggs

**Vanilla.** A thrown egg may spawn one chick, rarely four, or none.

**Mod.** The hatching roll always selects no chicks.

**Result.** Eggs remain throwable but cannot be used to produce chickens through their random hatch mechanic.

## Unbreaking

**Vanilla.** Each potential durability point may be prevented by the Unbreaking enchantment.

**Mod.** The prevention roll always fails.

**Result.** Unbreaking never saves durability. The enchantment is not removed and other durability rules remain unchanged.

## Bone meal

**Crops.** Vanilla chooses a small random growth amount. The mod chooses the minimum supported two-stage increase.

**Trees and fungi.** Vanilla growth attempts can fail without consuming or replacing their full growth path. The mod selects the existing runtime failure outcome for supported tree-like targets.

**Result.** Crop bone meal gives the least progress, while supported trees and fungi refuse random growth attempts.

## Chorus fruit

**Vanilla.** Up to sixteen random candidate teleports are attempted in generated order until one succeeds.

**Mod.** The same vanilla-valid candidates are evaluated and ranked from most dangerous to least dangerous before attempts.

**Result.** The fruit still uses vanilla collision, border, and destination validation, but favors falls, hazards, isolation, and other dangerous valid outcomes.

## Anvils

**Vanilla.** Eligible non-creative uses have a 12% chance to advance the anvil's damage state.

**Mod.** The roll always succeeds.

**Result.** Every eligible use damages the anvil; the final damaged state is destroyed. Creative-mode exemptions remain intact.

## Cat morning gifts

**Vanilla.** A tamed cat sleeping beside its owner can pass a random morning-gift roll and generate a gift table.

**Mod.** The gift roll always fails.

**Result.** Sleeping behavior remains, but no morning item is produced.

## Fox spawn items

**Vanilla.** A newly initialized fox has a 20% outer chance to receive an item, followed by a weighted item selection.

**Mod.** The 20% chance is preserved. If it succeeds, the selected stack is replaced with one egg.

**Result.** Fox item frequency stays vanilla, but successful foxes carry a low-value egg rather than emeralds, food, leather, or feathers.

## Villager offers

**Vanilla.** New offers are randomly sampled from profession/level factories and may contain favorable price and item combinations.

**Mod.** Newly generated offers use deterministic worst-case supported pools.

**Result.** New villagers offer unfavorable trades. Offers already serialized in a world are not rewritten.

## Wandering traders

**Vanilla.** A manager periodically rolls to spawn a wandering trader and trader llamas.

**Mod.** Automatic manager spawning is disabled.

**Result.** Existing traders and traders created by commands or other explicit systems remain valid, but normal automatic appearances stop.
