from pathlib import Path

p = Path("README.md")
s = p.read_text(encoding="utf-8")

s = s.replace(
    "- Player-fired projectiles choose the largest angular deviation permitted by vanilla projectile uncertainty; the former arbitrary ±180° yaw/pitch rotation has been removed.",
    "- Player-fired projectiles use a varied random direction at the maximum boundary of vanilla's per-axis uncertainty range; repeated shots with a fixed camera no longer converge on one deterministic point. The former arbitrary ±180° yaw/pitch rotation remains removed.",
)
s = s.replace(
    "- Снаряды игрока выбирают наибольшее угловое отклонение, допускаемое ванильной формулой неточности; прежний произвольный поворот на ±180° по горизонтали и вертикали удалён.",
    "- Снаряды игрока получают случайное направление отклонения на максимальной границе ванильного покоординатного диапазона неточности; повторные выстрелы при неподвижной камере больше не сходятся в одну детерминированную точку. Прежний произвольный поворот на ±180° по горизонтали и вертикали остаётся удалённым.",
)

english = """### Version 2.3.1 gameplay mechanics

Version **2.3.1** is a stable patch centered on destructive fire behavior, with several smaller worst-luck outcomes and diagnostics.

- Fire performs up to **eight additional successful spread changes per source fire tick** within vanilla's direct-neighbor and long-range spread volume. Direct flammable blocks, including TNT, are prioritized before valid air positions beside flammable blocks. The extra phase runs only while fire spreading is enabled and the source fire survived its vanilla tick.
- Fire can also appear in the air block at the feet of a nearby player or passive mob standing on a solid non-flammable block. These targets are checked first within the same bounded per-tick budget.
- Both regular and ominous trial spawners eject exactly **one baked potato** for each post-combat reward instead of a key, potion or other consumable. Trial vaults and seeded ominous combat-item selection are unchanged.
- Every eligible anvil output taken by a non-creative player advances the anvil to its next damage state, destroying a chipped anvil on its next eligible use. Creative-mode use remains exempt.
- Tamed cats can still sleep beside their owner but never give a morning gift.
- `/worstluck debug spawning` reports the invoking player's cached mob pressure: total and persistent mobs, total/near/distant hostiles, replacement budget, distant-reservoir protection and the farthest replaceable hostile distance. The command requires operator/game-master permission and a player source.
- Player projectiles now choose a random azimuth around the aim line before extending the error to the boundary of vanilla's per-axis uncertainty range. This preserves varied spread instead of sending fixed-camera shots toward one repeated point, and normalizes the final velocity to preserve projectile speed.

Testing priorities: dense fire around structures and TNT, fire near players/passive mobs on stone-like floors, repeated trial-spawner completions, all three anvil states, cat sleep cycles, fixed-camera projectile groups, and spawning diagnostics in multiplayer.

"""
russian = """### Игровые механики версии 2.3.1

Версия **2.3.1** — стабильное небольшое обновление, основная механика которого посвящена разрушительному огню; остальные изменения добавляют несколько худших исходов и диагностику.

- За каждый тик одного блока огня выполняется до **восьми дополнительных гарантированных распространений** в пределах ванильной области прямого и дальнего распространения. Сначала обрабатываются соседние горючие блоки, включая TNT, затем подходящие воздушные блоки рядом с горючими. Дополнительная фаза работает только при включённом распространении огня и если исходный огонь пережил свой ванильный тик.
- Огонь также может появиться в воздушном блоке у ног ближайшего игрока или мирного животного, стоящего на плотном негорючем блоке. Такие цели имеют приоритет в рамках того же ограниченного бюджета на тик.
- Обычный и зловещий рассадники испытаний после боя выбрасывают ровно **одну печёную картофелину** вместо ключа, зелья или другого расходника. Хранилища испытаний и сидированный выбор боевых предметов зловещего рассадника не изменены.
- Каждое подходящее извлечение результата из наковальни игроком не в творческом режиме переводит её на следующую стадию повреждения; повреждённая наковальня при следующем таком использовании уничтожается. Творческий режим не затронут.
- Приручённые кошки по-прежнему могут спать рядом с владельцем, но никогда не приносят утренний подарок.
- `/worstluck debug spawning` показывает кэш давления мобов для вызвавшего игрока: общее число мобов, число постоянных мобов, общее/ближнее/дальнее число врагов, бюджет замен, защиту дальнего резерва и расстояние до самого дальнего заменяемого врага. Команда требует прав оператора или ведущего игры и должна выполняться игроком.
- Снаряды игрока теперь выбирают случайный азимут вокруг линии прицеливания, после чего отклонение доводится до границы ванильного покоординатного диапазона неточности. Поэтому при неподвижной камере сохраняется разнообразный разброс вместо полёта в одну повторяющуюся точку, а итоговая скорость нормализуется и не меняет скорость снаряда.

Особенно важно проверить: плотный огонь около построек и TNT, огонь рядом с игроками и мирными животными на каменных полах, многократное завершение рассадников, все три состояния наковальни, циклы сна кошек, серии выстрелов при неподвижной камере и диагностику спавна в мультиплеере.

"""

if "### Version 2.3.1 gameplay mechanics" not in s:
    s = s.replace("### Modernized implementation", english + "### Modernized implementation", 1)
if "### Игровые механики версии 2.3.1" not in s:
    s = s.replace("### Современная реализация", russian + "### Современная реализация", 1)

s = s.replace("dist/worst-luck-possible-2.3.0.jar", "dist/worst-luck-possible-2.3.1.jar")
p.write_text(s, encoding="utf-8")
