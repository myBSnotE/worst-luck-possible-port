from pathlib import Path

path = Path("README.md")
text = path.read_text(encoding="utf-8")

en = '''### Experimental 2.3.0 beta mechanics

Version **2.3.0-beta.1** is a prerelease because it changes several independent vanilla RNG paths and needs focused gameplay testing.

- Every thrown Eye of Ender chooses its vanilla break outcome instead of dropping.
- A successful player Ender Pearl teleport always creates an endermite when monster spawning is allowed.
- Zombie-villager curing starts at the vanilla maximum of 6000 ticks and does not receive the random bed/iron-bar acceleration.
- Unbreaking never prevents durability loss. Mending is deliberately unchanged.
- Armor worn by mobs takes no durability damage, including direct sunlight wear on skeleton and zombie helmets. The protection belongs to the wearer logic rather than the item component, so dropped armor is still ordinarily damageable.
- Thrown eggs never hatch chicks.
- Bonemealed crops receive the minimum vanilla growth of two stages. Saplings, azaleas, fungi and mushrooms choose their existing random bonemeal failure result.
- Shulkers use the minimum random bullet interval of 20 ticks. Slimes use the minimum random jump delay of 10 ticks; magma cubes retain their vanilla four-times multiplier, giving 40 ticks.
- A witch always drinks the first applicable defensive potion under vanilla's existing condition order. For thrown potions, the favorable 25% close-range Weakness branch is suppressed, leaving Harming after the earlier Slowness and Poison rules.
- Ghast and blaze attack timing is unchanged: inspection of Minecraft 1.21.11 showed fixed, not random, attack intervals.
- Mob weapon enchantments are now limited to combinations valid for the item type: swords receive Sharpness V, Fire Aspect II and Knockback II; axes receive Sharpness V; tridents receive Impaling V, Loyalty III and Channeling I.

Testing priorities: mob helmets through long daytime sessions, armor damaged by combat, curing with and without beds/iron bars, repeated pearl and eye throws, witch decisions at different health and distance states, shulker fire rate, slime/magma-cube movement, and bonemeal targets.

'''
ru = '''### Экспериментальные механики беты 2.3.0

Версия **2.3.0-beta.1** выпущена как предварительная: она меняет несколько независимых путей ванильной случайности и требует отдельного игрового тестирования.

- Каждое брошенное Око Края выбирает ванильный исход с разрушением и не выпадает предметом.
- Успешная телепортация игрока жемчугом Края всегда создаёт эндермита, если разрешён спавн монстров.
- Лечение зомби-жителя начинается с ванильного максимума в 6000 тиков и не получает случайного ускорения от кроватей и железных решёток.
- Прочность всегда тратится, даже при наличии Прочности (Unbreaking). Починка (Mending) намеренно не изменена.
- Броня, надетая на мобов, не теряет прочность, включая прямой износ шлемов скелетов и зомби на солнце. Защита привязана к логике владельца, а не к компоненту предмета, поэтому выпавшая броня снова имеет обычную разрушаемость.
- Брошенные яйца никогда не создают цыплят.
- Удобренные костной мукой культуры получают минимальные ванильные две стадии роста. Саженцы, азалии, грибы Нижнего мира и обычные грибы выбирают уже существующий ванильный случайный провал костной муки.
- Шалкеры используют минимальный случайный интервал между пулями — 20 тиков. Слизни используют минимальную случайную задержку прыжка — 10 тиков; магмовые кубы сохраняют ванильный множитель ×4, то есть 40 тиков.
- Ведьма всегда выпивает первое подходящее защитное зелье согласно существующему ванильному приоритету условий. При броске зелий благоприятная для игрока 25%-я ближняя ветка Слабости отключена, поэтому после более ранних правил Замедления и Отравления остаётся Моментальный урон.
- Интервалы атак гастов и ифритов не изменены: проверка кода Minecraft 1.21.11 показала, что они фиксированы, а не случайны.
- Зачарования оружия мобов теперь ограничены допустимыми для предмета сочетаниями: мечи получают Остроту V, Заговор огня II и Отбрасывание II; топоры — Остроту V; трезубцы — Пронзатель V, Верность III и Громовержец I.

Особенно важно проверить: шлемы мобов в течение длинного дня, урон броне в бою, лечение с кроватями/решётками и без них, многократные броски жемчуга и Ока Края, решения ведьм при разном здоровье и расстоянии, частоту выстрелов шалкеров, движение слизней и магмовых кубов, а также разные цели костной муки.

'''
if "### Experimental 2.3.0 beta mechanics" not in text:
    text = text.replace("### Modernized implementation", en + "### Modernized implementation")
if "### Экспериментальные механики беты 2.3.0" not in text:
    text = text.replace("### Современная реализация", ru + "### Современная реализация")
text = text.replace("dist/worst-luck-possible-2.2.0.jar", "dist/worst-luck-possible-2.3.0-beta.1.jar")
path.write_text(text, encoding="utf-8")
