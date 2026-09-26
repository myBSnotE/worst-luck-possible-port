# Worst Luck Possible 2.0.1

## English

This update keeps distant hostile mobs available as a temporary reservoir while too few enemies have reached the player.

- Hostiles within 32 blocks remain protected from random despawning.
- Hostiles between 32 and 128 blocks are protected while fewer than 45% of eligible mobs are near their nearest player.
- Vanilla random despawning resumes after the near share exceeds 55%.
- Protection is disabled above 140 eligible hostiles per player as a performance safeguard.
- Counts are cached for one second and assigned by nearest player for multiplayer safety.
- Vanilla immediate despawning at 128 blocks remains unchanged.
- README installation instructions now direct players to GitHub Releases.

## Русский

В этом обновлении дальние враждебные мобы сохраняются как временный резерв, пока к игроку не приблизилось достаточное количество врагов.

- Мобы в радиусе 32 блоков по-прежнему защищены от случайного деспавна.
- Мобы на расстоянии от 32 до 128 блоков защищены, пока рядом с их ближайшим игроком находится менее 45% подходящих врагов.
- Ванильный случайный деспавн возобновляется после превышения доли в 55%.
- Защита отключается при количестве свыше 140 подходящих врагов на игрока как предохранитель от чрезмерной нагрузки.
- Подсчёты кэшируются на одну секунду и привязываются к ближайшему игроку для корректной работы в мультиплеере.
- Мгновенный ванильный деспавн на расстоянии 128 блоков не изменён.
- Инструкция по установке в README теперь направляет игроков во вкладку GitHub Releases.

## Requirements / Требования

- Minecraft **1.21.11**
- Fabric Loader **0.19.5** or newer / или новее
- Java **21**
- Fabric API is not required / Fabric API не требуется
