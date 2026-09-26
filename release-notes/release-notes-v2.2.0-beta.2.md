# Worst Luck Possible 2.2.0-beta.2

> **Prerelease / предварительная версия.** This patch builds on the tested beta.1 and needs focused raid and long-thunder testing.

## Русский

### Пополнение рейда ведьмами

- Когда в активной волне остаётся не более двух налётчиков, мод пытается добавить одну ведьму.
- Один пакет проверяет до **64 точек** в радиусе **32 блоков** от ближайшего игрока.
- Точка должна иметь освещение не выше **7**, плотный пол, два свободных блока, находиться в загруженной тикающей области рядом с занятой деревней и пройти ванильные проверки спавна ведьмы, столкновений и жидкостей.
- После успешного появления ведьмы число участников снова становится выше порога. После убийства следующего налётчика запускается новый пакет.
- Волна может завершиться только после провала всех 64 кандидатов очередного пакета.
- Состояние истощения сбрасывается для каждой новой волны.

### Ограничение лошадей-ловушек

- Новые ловушки не создаются, если в загруженном радиусе **128 блоков** от соответствующего игрока уже находится **96 или больше** живых персистирующих/недеспавнящихся мобов.
- Подсчёт использует общий секундный кэш плотности и не выполняет полный обход сущностей для каждой молнии.
- Сама молния продолжает ударять; отменяется только создание новой ловушки.

### Метаданные

- `fabric.mod.json` обновлён: современное имя и описание, автор оригинала и автор порта, ссылки на репозиторий и Issues, точные требования Fabric Loader 0.19.5+, Java 21+ и Minecraft 1.21.11.

## English

### Raid witch refills

- When an active wave has two or fewer raiders, the mod attempts to add one witch.
- One batch checks up to **64 positions** within **32 blocks** of the nearest player.
- A candidate requires light level **7 or lower**, a solid floor, two free blocks, loaded and ticking terrain near the occupied village, plus vanilla witch spawn, collision, and fluid validation.
- A successful witch raises the wave above the threshold. Killing another raider starts another batch.
- The wave may finish only after all 64 candidates in the current batch fail.
- Exhaustion state resets for every new wave.

### Skeleton-horse trap cap

- New traps are suppressed when the relevant player already has **96 or more** living persistent/non-despawning mobs inside the loaded 128-block radius.
- The count uses the shared one-second density cache rather than scanning all entities for every strike.
- Lightning still strikes normally; only the new trap group is skipped.

### Metadata

- Updated `fabric.mod.json` with current naming and description, original and port credits, repository/Issues links, and precise Fabric Loader 0.19.5+, Java 21+, and Minecraft 1.21.11 requirements.
