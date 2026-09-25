# Worst Luck Possible 2.2.0

Stable Minecraft 1.21.11 Fabric release based on the tested 2.2.0 beta series.

## Русский

### Естественный спавн и производительность

- Враждебные мобы по-прежнему предпочитают естественный спавн в полосе 24–32 блока от игрока.
- Исправлена блокировка пустого моб-капа: если у игрока нет ни одного закреплённого врага, один моб может появиться в обычном ванильном радиусе за пределами 32 блоков.
- После первого успешного дальнего спавна кэш немедленно обновляется, и следующие попытки снова ищут близкие позиции.
- Одиночный дальний моб сохраняет ванильный случайный деспавн; защита дальнего резерва начинается только от восьми врагов.
- При полном моб-капе дальний враг удаляется только после успешного появления корректной близкой замены.
- Общий секундный кэш плотности, адаптивный бюджет замены и распределённый поиск пути снижают нагрузку.

### Мобы, рейды и перемещение

- Пауки на высокой сложности получают Скорость I; естественные слизни и магмовые кубы имеют максимальный размер.
- Броня, луки, арбалеты и оружие появившихся мобов получают худшие для игрока максимальные боевые зачарования текущей реализации.
- Лошади, ослы и потомство семейства лошадей получают минимальные ванильные характеристики.
- Зомби на высокой сложности ломают двери, а подкрепления выбирают корректные позиции ближе к игроку.
- Рейды получают максимальный ванильный бонусный состав и дополнительную ведьму через безопасную замену дальнего врага.
- При двух и менее участниках волна постоянно пополняется ведьмами; при нуле участников проверяются пять независимых пакетов по 64 позиции перед завершением волны.
- Плод хоруса ранжирует 16 ванильных кандидатов и выбирает наиболее опасную допустимую позицию.

### Грозы и лошади-ловушки

- Лошадь-ловушка появляется гарантированно только рядом с игроком в том же чанке.
- Создающая её молния не убивает ловушку.
- Новые ловушки блокируются при 96 живых персистирующих/недеспавнящихся мобах в радиусе 128 блоков.
- Команды временного снижения частоты молний сохранены.

### Метаданные

- Обновлён `fabric.mod.json`, описание, авторство, ссылки и требования платформы.
- Требуются Minecraft 1.21.11, Fabric Loader 0.19.5+ и Java 21; Fabric API не требуется.

## English

### Natural spawning and performance

- Hostiles still prefer natural spawn positions 24–32 blocks from the player.
- Fixed empty-cap deadlock: when a player has no assigned hostiles, one mob may spawn at ordinary vanilla range beyond 32 blocks.
- The density cache is invalidated immediately after that seed spawn, returning subsequent attempts to the close band.
- A lone distant seed retains vanilla random despawning; distant-reservoir protection starts at eight hostiles.
- At a full mob cap, a distant hostile is removed only after a valid close replacement successfully spawns.
- Shared one-second density caching, adaptive replacement budgets, and staggered pathfinding reduce load.

### Mobs, raids, and movement

- Spiders gain Speed I on Hard; naturally spawned slimes and magma cubes use maximum size.
- Spawned mob armor, bows, crossbows, and melee weapons receive the current implementation's worst maximum combat enchantments.
- Horses, donkeys, and horse-family offspring receive minimum vanilla attributes.
- Zombies break doors on Hard and reinforcements choose valid positions closer to the player.
- Raids receive maximum vanilla bonus composition and an extra witch through a safe distant-hostile swap.
- Waves at two or fewer raiders continuously refill with witches; at zero raiders, five independent 64-position batches run before the wave may finish.
- Chorus fruit ranks sixteen vanilla-valid candidates and attempts the most dangerous position first.

### Storms and skeleton-horse traps

- A trap horse is guaranteed only beside a player in the same chunk.
- Its creating lightning cannot kill the trap.
- New traps are suppressed at 96 living persistent/non-despawning mobs within 128 blocks.
- Temporary lightning-rate commands remain available.

### Metadata

- Updated `fabric.mod.json`, description, credits, links, and platform requirements.
- Requires Minecraft 1.21.11, Fabric Loader 0.19.5+, and Java 21; Fabric API is not required.
