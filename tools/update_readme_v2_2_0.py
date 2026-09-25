from pathlib import Path

path = Path("README.md")
text = path.read_text(encoding="utf-8")
text = text.replace("2.2.0-beta.3", "2.2.0")
text = text.replace("### Experimental 2.2.0 beta mechanics", "### Version 2.2.0 gameplay mechanics")
text = text.replace("### Экспериментальные механики беты 2.2.0", "### Игровые механики версии 2.2.0")
text = text.replace(
    "Version **2.2.0** is intentionally published as a prerelease. The breadth of the AI, raid, entity and teleport changes requires multiplayer and long-session testing before a stable 2.2.0 release.",
    "Version **2.2.0** promotes the tested beta mechanics to the stable release, including the final natural-spawn fallback and recurring raid-witch fixes."
)
text = text.replace(
    "Версия **2.2.0** намеренно публикуется как предварительная. Изменения ИИ, рейдов, сущностей и телепортации требуют длительного тестирования, особенно в мультиплеере, прежде чем появится стабильная 2.2.0.",
    "Версия **2.2.0** переносит проверенные механики беты в стабильный релиз, включая итоговые исправления резервного естественного спавна и повторного появления рейдовых ведьм."
)
old_en = "While the hostile mob count is below the normal cap, no existing mob is removed: ordinary natural spawn attempts continue and the proximity logic tries to place valid hostile spawns close to a nearby player, normally within **24–32 blocks**. All normal block, light, biome, collision and spawn-restriction checks still apply, so spawning can occur farther away when no close position is valid."
new_en = "While the hostile mob count is below the normal cap, no existing mob is removed: ordinary natural spawn attempts prefer valid hostile positions **24–32 blocks** from a nearby player. If that player's hostile population is completely empty, one ordinary vanilla-range spawn may seed the cycle outside 32 blocks. Its density snapshot is invalidated immediately, so later attempts return to the close band. A lone distant seed retains vanilla random despawning and can disappear before another fallback attempt searches again. All normal block, light, biome, collision and spawn-restriction checks still apply."
text = text.replace(old_en, new_en)
old_ru = "Пока количество враждебных мобов ниже обычного моб-капа, существующие мобы не удаляются: обычные попытки естественного спавна продолжаются, а логика близкого спавна старается разместить подходящих врагов рядом с игроком — обычно на расстоянии **24–32 блоков**. Все обычные проверки блоков, освещения, биома, столкновений и ограничений спавна сохраняются, поэтому при отсутствии подходящей близкой позиции мобы могут появляться дальше."
new_ru = "Пока количество враждебных мобов ниже обычного моб-капа, существующие мобы не удаляются: естественный спавн предпочитает подходящие позиции на расстоянии **24–32 блоков** от ближайшего игрока. Если закреплённых за игроком враждебных мобов совсем нет, один моб может появиться в обычном ванильном радиусе за пределами 32 блоков и запустить цикл. Кэш плотности сразу сбрасывается, поэтому следующие попытки снова направляются в ближнюю полосу. Одиночный дальний моб сохраняет ванильный случайный деспавн и может исчезнуть, после чего резервная попытка повторит поиск. Все обычные проверки блоков, освещения, биома, столкновений и ограничений спавна сохраняются."
text = text.replace(old_ru, new_ru)
text = text.replace(
    "- Vanilla immediate despawning at 128 blocks is unchanged.",
    "- Distant-reservoir protection starts only at eight assigned hostiles, so an initial fallback mob keeps vanilla random despawning.\n- Vanilla immediate despawning at 128 blocks is unchanged."
)
text = text.replace(
    "- Мгновенный ванильный деспавн за пределами 128 блоков не изменён.",
    "- Защита дальнего резерва включается только при наличии не менее восьми закреплённых врагов, поэтому первый резервный моб сохраняет ванильный случайный деспавн.\n- Мгновенный ванильный деспавн за пределами 128 блоков не изменён."
)
path.write_text(text, encoding="utf-8")
