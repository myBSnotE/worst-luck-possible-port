from pathlib import Path

path = Path("README.md")
text = path.read_text(encoding="utf-8")

text = text.replace(
    "- Every spider becomes a spider jockey; on Hard the rider is invisible.",
    "- Every spider becomes a spider jockey; on Hard the spider has permanent Speed I and the rider is invisible.",
)
text = text.replace(
    "- Replacement work is limited to four mobs per tick to avoid excessive server load.",
    "- Replacement work is limited to at most four mobs per tick and automatically drops to two, one or zero as nearby entity pressure rises.",
)
text = text.replace(
    "### Modernized implementation\n",
    """### Experimental 2.2.0 beta mechanics

Version **2.2.0-beta.1** is intentionally published as a prerelease. The breadth of the AI, raid, entity and teleport changes requires multiplayer and long-session testing before a stable 2.2.0 release.

- Spiders on Hard receive permanent Speed I. Because every spider is a jockey, this helps the skeleton rider close distance instead of duplicating its already high damage.
- Naturally spawned slimes and magma cubes use the largest vanilla natural size.
- Spawned mob armor receives Protection IV and Thorns III. Bows receive Power V, Punch II and Flame I; crossbows receive Quick Charge III and Piercing IV; melee weapons receive maximum combat enchantments appropriate to the current implementation.
- Naturally generated horses use the vanilla minimums: 15 health, 0.1125 movement speed and 0.4 jump strength. Bred horse-family children also receive these minimums; naturally generated donkeys receive the minimum random health while retaining their fixed vanilla movement and jump attributes.
- Zombies on Hard always break doors when their navigation supports it. Reinforcement coordinates are biased toward a nearby relevant player while retaining vanilla spawn, collision, fluid and minimum-distance validation.
- Raid bonus rolls always choose their vanilla maximum. After each wave, one additional witch can join only when a distant non-persistent hostile can be replaced one-for-one. Raid members and protected mobs are never selected, so the swap does not inflate the entity count.
- Chorus fruit still generates sixteen candidates inside its vanilla 16-block diameter and uses the ordinary teleport validator, but tries the candidates from most dangerous to least dangerous. Darkness, nearby hostiles, hazards, drops, lower elevation and displacement affect the ranking; a random tie component prevents deterministic control.
- Skeleton-horse riders inherit the same maximum bow and armor enchantment policy as other equipped hostile mobs.
- Hostile density, reinforcement limits, distant-reservoir decisions and cap replacement share a one-second cache. Player-seeking mobs share a bounded, staggered path-search budget rather than recalculating paths together.
- Lightning intensity is deliberately unchanged. Full mode continues to create the intended environmental destruction; the existing temporary 20× reduction command remains available to operators.
- Patrol spawning remains vanilla in this beta because guaranteeing captains would also guarantee a useful ominous-bottle source.

Testing priorities: multiplayer density around separate players, raids through every difficulty and wave, zombie reinforcement placement, horse breeding, chorus fruit in caves/Nether/End, skeleton traps, and long thunder sessions.

### Modernized implementation
""",
    1,
)

text = text.replace(
    "- Каждый паук появляется с наездником; на высокой сложности наездник невидим.",
    "- Каждый паук появляется с наездником; на высокой сложности паук получает постоянную Скорость I, а наездник становится невидимым.",
)
text = text.replace(
    "- За один тик заменяется не более четырёх мобов, чтобы избежать чрезмерной нагрузки на сервер.",
    "- За один тик заменяется не более четырёх мобов; при росте числа ближайших сущностей бюджет автоматически снижается до двух, одной или нуля замен.",
)
text = text.replace(
    "### Современная реализация\n",
    """### Экспериментальные механики беты 2.2.0

Версия **2.2.0-beta.1** намеренно публикуется как предварительная. Изменения ИИ, рейдов, сущностей и телепортации требуют длительного тестирования, особенно в мультиплеере, прежде чем появится стабильная 2.2.0.

- Пауки на высокой сложности получают постоянную Скорость I. Поскольку каждый паук несёт скелета, ускорение помогает всаднику догнать игрока вместо лишнего усиления и без того большого урона.
- Естественно появившиеся слизни и магмовые кубы получают максимальный ванильный естественный размер.
- Броня заспавненных мобов получает Защиту IV и Шипы III. Луки получают Силу V, Откидывание II и Горящую стрелу I; арбалеты — Быструю перезарядку III и Пронзающую стрелу IV; оружие ближнего боя — максимальные боевые зачарования текущей реализации.
- Естественные лошади получают ванильные минимумы: 15 здоровья, скорость 0,1125 и силу прыжка 0,4. Потомки семейства лошадей также получают эти минимумы; естественные ослы получают минимальное случайное здоровье, сохраняя фиксированные ванильные скорость и прыжок.
- На высокой сложности зомби всегда ломают двери, если это поддерживает их навигация. Координаты подкреплений смещаются ближе к соответствующему игроку, но ванильные проверки места, столкновений, жидкостей и минимальной дистанции сохраняются.
- Бонусный состав рейда всегда получает ванильный максимум. После каждой волны может присоединиться одна дополнительная ведьма, но только при возможности заменить дальнего непостоянного врага один к одному. Участники рейда и защищённые мобы не удаляются, поэтому общее число сущностей не растёт.
- Плод хоруса по-прежнему создаёт 16 кандидатов внутри ванильного диаметра 16 блоков и использует обычную проверку телепортации, но пробует точки от самой опасной к наименее опасной. Учитываются темнота, ближайшие враги, опасные блоки, обрывы, понижение высоты и удаление; случайная добавка не даёт полностью контролировать результат.
- Всадники лошадей-ловушек получают ту же политику максимальных зачарований лука и брони, что и остальные экипированные враждебные мобы.
- Плотность врагов, лимит подкреплений, защита дальнего резерва и замена при моб-капе используют общий секундный кэш. Идущие к игроку мобы делят ограниченный и распределённый по тикам бюджет поиска пути.
- Интенсивность молний намеренно не уменьшена. Полный режим сохраняет требуемое разрушение окружения; существующая временная команда снижения частоты в 20 раз остаётся доступной операторам.
- Патрули в этой бете не изменены: гарантированный капитан одновременно гарантировал бы полезный источник зловещих бутылок.

Особенно важно проверить: плотность мобов вокруг разных игроков, все волны рейдов на разных сложностях, позиции подкреплений, разведение лошадей, плод хоруса в пещерах/Незере/Крае, ловушки-лошади и длительные грозы.

### Современная реализация
""",
    1,
)

text = text.replace("dist/worst-luck-possible-2.1.1.jar", "dist/worst-luck-possible-2.2.0-beta.1.jar")
path.write_text(text, encoding="utf-8")
