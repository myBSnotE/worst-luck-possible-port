# Worst Luck Possible 2.2.0-beta.1

> **Prerelease / предварительная версия.** Keep v2.1.1 available for stable worlds and back up worlds before testing.

## Русский

Эта бета предназначена для тестирования крупного набора механик перед стабильной 2.2.0.

### Мобы и транспорт

- Пауки на высокой сложности получают постоянную **Скорость I**; гарантированные скелеты-наездники сохраняются.
- Естественные слизни и магмовые кубы получают максимальный ванильный естественный размер.
- Заспавненная броня мобов получает Защиту IV и Шипы III; луки, арбалеты и оружие ближнего боя получают максимальные боевые зачарования текущей реализации.
- Лошади получают минимальные ванильные здоровье, скорость и прыжок; эти минимумы применяются и при разведении. Для естественных ослов минимизируется случайное здоровье.
- Зомби на высокой сложности ломают двери и пытаются размещать подкрепления ближе к соответствующему игроку, сохраняя ванильные проверки спавна.
- Всадники лошадей-ловушек получают максимальные зачарования луков и шлемов через общую систему экипировки мобов.

### Рейды

- Случайные бонусы состава каждой волны всегда выбирают ванильный максимум.
- После волны может добавиться одна ведьма, только если можно атомарно заменить дальнего непостоянного врага.
- Рейдовые мобы, именованные и защищённые сущности не удаляются; без кандидата ведьма не появляется.
- Патрули не изменены, чтобы не создавать гарантированный источник зловещих бутылок.

### Плод хоруса

- Сохраняются ванильные 16 кандидатов, радиус и финальная проверка телепортации.
- Сначала пробуются наиболее опасные точки: рядом с врагами и опасными блоками, в темноте, ниже игрока, у обрывов и дальше от исходной позиции.
- Случайная добавка к оценке мешает полностью контролировать результат.

### Производительность

- Добавлен общий секундный кэш плотности мобов для деспавна, подкреплений, замены моб-капа и рейдовых ведьм.
- Бюджет замены снижается с четырёх до двух, одной или нуля операций при росте нагрузки.
- Поиски пути к игрокам распределены по тикам и ограничены общим бюджетом мира.
- Частота молний не снижена; временные команды `lightning reduced/full` сохранены.

### Что тестировать

- отдельные группы игроков в мультиплеере;
- все сложности и волны рейдов;
- появление и завершение волн с дополнительной ведьмой;
- подкрепления зомби в пещерах и зданиях;
- разведение лошадей, ослов и мулов;
- плод хоруса в пещерах, Незере и Крае;
- ловушки-лошади и длительные грозы;
- длительную нагрузку сервера и моб-кап.

## English

This prerelease is for testing the large 2.2 mechanics set before a stable release.

### Mobs and mounts

- Spiders on Hard receive permanent **Speed I** while guaranteed skeleton jockeys remain enabled.
- Natural slimes and magma cubes use the largest vanilla natural size.
- Spawned mob armor receives Protection IV and Thorns III; bows, crossbows and melee equipment receive maximum combat enchantments from the current implementation.
- Horses receive minimum vanilla health, movement and jump values, including through breeding. Natural donkeys receive minimum random health.
- Zombies on Hard break doors and bias reinforcement attempts toward the relevant player while retaining vanilla spawn checks.
- Skeleton-horse riders receive maximum bow and helmet enchantments through the shared mob-equipment policy.

### Raids

- Random wave bonuses always choose their vanilla maximum.
- A wave may gain one extra witch only by atomically replacing a distant non-persistent hostile.
- Raid members, named mobs and protected entities are not removed; no candidate means no extra witch.
- Patrols remain vanilla to avoid creating a guaranteed ominous-bottle source.

### Chorus fruit

- Vanilla's sixteen candidates, diameter and final teleport validator remain in use.
- The most dangerous candidates are attempted first: nearby hostiles and hazards, darkness, lower elevations, ledges and greater displacement all raise the score.
- Random score jitter prevents complete deterministic control.

### Performance

- Despawning, reinforcements, mob-cap replacement and raid witches share a one-second density cache.
- Replacement budget scales from four to two, one or zero operations as nearby load rises.
- Player-seeking path searches are staggered and share a per-world budget.
- Lightning intensity is not reduced; the temporary `lightning reduced/full` commands remain available.

### Testing priorities

Test separated multiplayer groups, every raid difficulty/wave, extra-witch wave completion, reinforcements inside structures and caves, horse-family breeding, chorus fruit in caves/the Nether/the End, skeleton traps, long thunderstorms, and long-session mob-cap performance.
