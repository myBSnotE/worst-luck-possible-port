# Worst Luck Possible — Minecraft 1.21.11 port

<details>
<summary><strong>🇬🇧 English</strong></summary>

A modern Fabric port of **Worst Luck Possible**, a challenge mod that removes favorable randomness and consistently gives the player the worst practical outcome.

This repository ports the original Minecraft 1.16.1 mod to **Minecraft 1.21.11** while preserving its gameplay intent and adapting the implementation to current Minecraft internals.

## Original mod and creator

- **Creator:** Heppe
- **Original mod:** [Worst Luck Possible on Modrinth](https://modrinth.com/mod/worst-luck-possible)
- **Creator's original video:** [Is it possible to beat Minecraft with only bad RNG?](https://www.youtube.com/watch?v=LYmyuoRJecA)
- **Creator's channel:** [HeppeGaming](https://youtube.com/c/HeppeGaming)
- **Original target:** Minecraft 1.16.1 with Fabric

The original Modrinth listing describes the project as making Minecraft as difficult as possible without adding outcomes that are impossible in vanilla. The listing currently identifies the project as MIT-licensed, while the metadata embedded in the supplied 1.0.0 JAR declares CC0-1.0.

The original compiled JAR used as the behavioral reference is included at [`worst-luck-possible-1.0.0.jar`](worst-luck-possible-1.0.0.jar).

## Requirements

- Minecraft **1.21.11**
- Fabric Loader **0.19.5** or a newer compatible version
- Java **21**
- Fabric API is **not required**

## Installation

1. Install Fabric Loader for Minecraft 1.21.11.
2. Open the repository's [Releases page](https://github.com/myBSnotE/worst-luck-possible-port/releases/latest) and download the latest `worst-luck-possible-*.jar` asset.
3. Put the JAR into the instance or server `mods` directory.
4. Start the game or server.

The mod works on dedicated servers and supports multiple players. The original Modrinth release is marked singleplayer-only; multiplayer support is an addition of this port.

## Original gameplay features

### Loot

- Mob and block loot rolls choose the smallest possible result.
- Gravel never drops flint.
- Piglin bartering always gives two magma cream.
- Block drops scatter away from the nearest player.

### Hostile mobs

- Zombies and their variants always spawn as babies, wear the strongest equipment possible in vanilla, and are leaders.
- Zombie leaders reliably call reinforcements when damaged on Hard, unless the relevant player already has at least 140 living mobs within 128 blocks.
- Every spider becomes a spider jockey; on Hard the rider is invisible.
- Endermen teleport along the direction the player is looking.
- The Ender Dragon does not perch and keeps an unfavorable flight height.
- Hostile mobs actively path toward nearby players.

### Natural spawning

- Passive mobs do not spawn naturally during ordinary mob-spawn cycles.
- Fish and dolphins are disabled.
- Hostile mobs spawn in the largest practical packs with additional spawn attempts.
- Natural hostile spawns are kept close to a valid nearby player while retaining normal block, light, biome and collision checks.
- Drowned are made more common through hostile spawn substitution.

### Other

- Projectiles receive strongly randomized inaccuracy.
- Thunderstorms begin frequently, with lightning attempts across loaded chunks. Operators can temporarily reduce those attempts by 20× for the current server session.

## Additions and changes in this port

### Close spawning and adaptive hostile mob-cap replacement

While the hostile mob count is below the normal cap, no existing mob is removed: ordinary natural spawn attempts continue and the proximity logic tries to place valid hostile spawns close to a nearby player, normally within **24–32 blocks**. All normal block, light, biome, collision and spawn-restriction checks still apply, so spawning can occur farther away when no close position is valid.

When the hostile mob cap is full, the mod may temporarily allow a valid close spawn. Only after that spawn succeeds does it remove the farthest eligible hostile between 32 and 128 blocks from its nearest player.

- Below the cap, new close mobs can spawn without deleting an existing mob.
- At the cap, a distant mob is removed only after a valid closer replacement has actually spawned.
- Nothing is removed if a valid closer replacement cannot spawn.
- Named, persistent and otherwise protected mobs are never selected.
- Distance is always measured to each mob's **nearest player**, so multiplayer users cannot cause mobs to disappear in front of one another.
- Replacement work is limited to four mobs per tick to avoid excessive server load.
- Ordinary immediate despawning remains at 128 blocks; mobs around farms or platforms 64 blocks away are not forcibly deleted.

### Adaptive distant-hostile reservoir

Hostiles between 32 and 128 blocks are temporarily protected from vanilla random despawning while fewer than 45% of the eligible hostiles assigned to their nearest player are within 32 blocks. Normal random despawning resumes after the near share rises above 55%.

- Counts are assigned using each mob's nearest player, making the policy multiplayer-safe.
- Density is sampled once per second to limit server load.
- The 45%/55% hysteresis prevents rapid switching around a single threshold.
- Protection is disabled above 140 eligible hostiles per player as an emergency performance limit.
- Vanilla immediate despawning at 128 blocks is unchanged.

### Lightning-rate control

Operators can use `/worstluck lightning reduced` to change each ticking chunk from one lightning attempt every tick to a **5% chance per tick** — an average 20× reduction. `/worstluck lightning full` restores the original every-tick behavior, and `/worstluck lightning` shows the current mode. The setting is temporary and resets to full intensity when the world is reopened or the dedicated server restarts.

### Worst-luck fishing and temporary debug override

By default, every successful fishing catch yields exactly **one pair of leather boots with zero remaining durability** instead of using the fishing loot table. This removes fishing as an unseeded source of favorable randomness and prevents it from supplying fish, treasure, enchanted books or leather through ordinary fishing loot.

The initial random bite-wait roll is always its maximum vanilla value: **600 ticks (30 seconds)** before environmental and equipment modifiers. Rain, sky access, Lure and all later fishing phases retain their normal influence.

Operators can temporarily restore vanilla fishing for the current world/server session:

```text
/worstluck fishing vanilla
```

Additional commands:

- `/worstluck fishing` — show the current fishing mode;
- `/worstluck fishing boots` — immediately restore forced leather boots.

The override is not saved. Forced leather boots are enabled again after closing and reopening a singleplayer world or restarting the dedicated server. On a dedicated server, merely disconnecting a player does not restart the server session. These commands require game-master/operator permission.

### Guaranteed phantom attacks

After a player has avoided sleep for three in-game days, phantom attacks are guaranteed every **1200–1400 ticks** when vanilla-compatible conditions are satisfied:

- the player is at Y 64 or higher;
- the sky is visible above the player;
- it is sufficiently dark for the vanilla phantom spawner;
- phantom spawning and hostile spawning are enabled;
- the player is not a spectator;
- the selected spawn position is clear.

Each eligible player receives a pack of **four phantoms**, spawning **20–34 blocks above** with up to **9 blocks of horizontal offset**. Eligibility is evaluated independently for every player.

### Deterministic worst villager trades

Newly generated villager offers now use deterministic worst-case pools. The selected item pairs follow the real Minecraft 1.21.11 trade tables, prices of randomly enchanted equipment are fixed at their vanilla maxima, and enchantments are fixed to Fire Protection I (armor), Bane of Arthropods I (weapons), Punch I (bows), Efficiency I (tools), Piercing I (crossbows), and Curse of Vanishing I enchanted books for 38 emeralds plus a book.

The requested profession/level choices are applied to armorers, butchers, cartographers, clerics, farmers, fishermen, fletchers, leatherworkers, librarians, toolsmiths, shepherds, masons, and weaponsmiths. The mason's black/gray ceramics are ordinary black and gray terracotta; fletchers sell long-invisibility tipped arrows. Pools not explicitly changed remain vanilla.

Important 1.21.11 differences from older trade descriptions:

- cartographer level 3 also contains a Trial Chambers map candidate, which is now excluded in favor of the ocean explorer map;
- cartographer level 2 has biome/type-aware village, swamp and jungle maps, so not every other cartographer offer is fixed;
- fisherman level 3 still has a randomly enchanted fishing rod;
- librarian level 4 also had a clock candidate, which is excluded by the requested compass choice.

Trades already stored on existing villagers are not rewritten, because doing so would reset uses, demand and discounts and could destroy earlier-level offers. Use new villagers, or villagers whose affected level has not generated its offers yet, for the complete effect.

### Wandering traders

Automatic wandering-trader spawning (including its trader llamas) is disabled. Existing traders, `/summon`, and spawn eggs are not affected.

### Modernized implementation

- Ported mixins and mappings to Minecraft 1.21.11/Yarn.
- Updated to Java 21 and the current Fabric toolchain.
- Added multiplayer-safe nearest-player distance handling.
- Added automated GitHub Actions builds and dedicated-server smoke tests.

## Building

The repository uses Gradle and Fabric Loom. CI builds the mod and writes the remapped artifact to:

```text
dist/worst-luck-possible-2.1.0.jar
```

End users should download published builds from the [Releases page](https://github.com/myBSnotE/worst-luck-possible-port/releases/latest), not from the repository's `dist` directory.

The workflow also launches a temporary Fabric dedicated server to catch mixin application failures and startup crashes.

## Disclaimer

This is an unofficial modernization of the original mod. Minecraft is a trademark of Mojang Studios. This project is not affiliated with or endorsed by Mojang Studios.

</details>

<details open>
<summary><strong>🇷🇺 Русский</strong></summary>

Современный Fabric-порт **Worst Luck Possible** — челлендж-мода, который убирает благоприятную случайность и практически всегда выдаёт игроку худший из возможных результатов.

Этот репозиторий переносит оригинальный мод с Minecraft 1.16.1 на **Minecraft 1.21.11**, сохраняя его игровой замысел и адаптируя реализацию к современному внутреннему устройству Minecraft.

## Оригинальный мод и создатель

- **Создатель:** Heppe
- **Оригинальный мод:** [Worst Luck Possible на Modrinth](https://modrinth.com/mod/worst-luck-possible)
- **Оригинальное видео автора:** [Is it possible to beat Minecraft with only bad RNG?](https://www.youtube.com/watch?v=LYmyuoRJecA)
- **Канал автора:** [HeppeGaming](https://youtube.com/c/HeppeGaming)
- **Исходная версия:** Minecraft 1.16.1 с Fabric

В описании на Modrinth проект представлен как попытка сделать Minecraft максимально сложным, не добавляя исходы, невозможные в ванильной игре. Сейчас на странице указана лицензия MIT, тогда как метаданные приложенного JAR версии 1.0.0 указывают CC0-1.0.

Оригинальный скомпилированный JAR, использованный как эталон поведения, находится в файле [`worst-luck-possible-1.0.0.jar`](worst-luck-possible-1.0.0.jar).

## Требования

- Minecraft **1.21.11**
- Fabric Loader **0.19.5** или более новая совместимая версия
- Java **21**
- Fabric API **не требуется**

## Установка

1. Установите Fabric Loader для Minecraft 1.21.11.
2. Откройте [страницу Releases](https://github.com/myBSnotE/worst-luck-possible-port/releases/latest) репозитория и скачайте последний файл `worst-luck-possible-*.jar`.
3. Поместите JAR в папку `mods` клиента или сервера.
4. Запустите игру или сервер.

Мод работает на выделенных серверах и поддерживает нескольких игроков. Оригинальный релиз на Modrinth отмечен как предназначенный только для одиночной игры; поддержка мультиплеера добавлена в этом порте.

## Оригинальные игровые механики

### Лут

- Из блоков и мобов всегда выбирается минимально возможное количество лута.
- Из гравия никогда не выпадает кремень.
- Пиглины при обмене всегда выдают две единицы сгустка магмы.
- Выпавшие из блоков предметы разлетаются в стороны от ближайшего игрока.

### Враждебные мобы

- Зомби и их разновидности всегда появляются детьми, в максимально сильной экипировке, какая возможна в ванилле, и являются лидерами.
- Зомби-лидеры гарантированно вызывают подкрепление при получении урона на высокой сложности, если только в радиусе 128 блоков от соответствующего игрока ещё нет 140 живых мобов.
- Каждый паук появляется с наездником; на высокой сложности наездник невидим.
- Эндермены телепортируются вдоль направления взгляда игрока.
- Дракон Края не садится и сохраняет невыгодную для игрока высоту полёта.
- Враждебные мобы активно прокладывают путь к ближайшим игрокам.

### Естественный спавн

- Мирные животные не появляются естественным образом во время обычных циклов спавна мобов.
- Спавн рыб и дельфинов отключён.
- Враждебные мобы появляются максимально большими стаями с дополнительными попытками спавна.
- Естественный спавн врагов старается размещать их рядом с подходящим игроком, сохраняя обычные проверки блоков, освещения, биома и столкновений.
- Утопленники встречаются чаще благодаря подмене враждебных мобов при спавне в воде.

### Прочее

- Снаряды получают сильно рандомизированную неточность.
- Грозы начинаются часто, а в загруженных чанках регулярно предпринимаются попытки удара молнии. Оператор может временно снизить частоту этих попыток в 20 раз для текущей серверной сессии.

## Дополнения и изменения порта

### Близкий спавн и адаптивная замена мобов при заполненном моб-капе

Пока количество враждебных мобов ниже обычного моб-капа, существующие мобы не удаляются: обычные попытки естественного спавна продолжаются, а логика близкого спавна старается разместить подходящих врагов рядом с игроком — обычно на расстоянии **24–32 блоков**. Все обычные проверки блоков, освещения, биома, столкновений и ограничений спавна сохраняются, поэтому при отсутствии подходящей близкой позиции мобы могут появляться дальше.

Когда моб-кап враждебных существ заполнен, мод может временно разрешить корректную попытку близкого спавна. Только после успешного появления нового моба удаляется самый далёкий подходящий враг, находящийся на расстоянии от 32 до 128 блоков от ближайшего к нему игрока.

- Пока моб-кап не заполнен, новые близкие мобы появляются без удаления существующего моба.
- При заполненном моб-капе дальний моб удаляется только после фактического успешного появления более близкой замены.
- Если более близкого моба нельзя заспавнить, никто не удаляется.
- Именованные, постоянные и иным образом защищённые мобы никогда не выбираются для удаления.
- Расстояние всегда считается до **ближайшего к конкретному мобу игрока**, поэтому в мультиплеере один игрок не может вызвать исчезновение моба перед другим.
- За один тик заменяется не более четырёх мобов, чтобы избежать чрезмерной нагрузки на сервер.
- Обычный мгновенный деспавн остаётся на расстоянии 128 блоков; мобы вокруг ферм и платформ в 64 блоках от игрока не удаляются принудительно.

### Адаптивный резерв дальних мобов

Враждебные мобы на расстоянии от 32 до 128 блоков временно защищены от случайного ванильного деспавна, пока менее 45% подходящих врагов, закреплённых за ближайшим игроком, находится в радиусе 32 блоков. Обычный случайный деспавн возобновляется, когда доля ближних мобов превышает 55%.

- Каждый моб учитывается относительно ближайшего игрока, поэтому логика безопасна для мультиплеера.
- Плотность пересчитывается раз в секунду для снижения нагрузки на сервер.
- Гистерезис 45%/55% предотвращает постоянное переключение около одного порога.
- При количестве свыше 140 подходящих враждебных мобов на игрока защита отключается как аварийное ограничение производительности.
- Мгновенный ванильный деспавн за пределами 128 блоков не изменён.

### Управление частотой молний

Оператор может использовать `/worstluck lightning reduced`, чтобы заменить одну попытку удара в каждом тикающем чанке каждый тик на **5%-й шанс каждый тик** — в среднем это снижение частоты в 20 раз. `/worstluck lightning full` возвращает исходный режим с попыткой каждый тик, а `/worstluck lightning` показывает текущий режим. Настройка временная и сбрасывается на полную интенсивность после повторного открытия мира или перезапуска выделенного сервера.

### Худшая рыбалка и временное отладочное отключение

По умолчанию каждый успешный улов гарантированно даёт ровно **одну пару кожаных ботинок с нулевой оставшейся прочностью** вместо использования таблицы рыболовного лута. Это убирает рыбалку как несидированный источник благоприятной случайности и не позволяет получать через обычный рыболовный лут рыбу, сокровища, зачарованные книги или кожу.

Начальная случайная длительность ожидания клёва всегда получает максимальное ванильное значение: **600 тиков (30 секунд)** до применения модификаторов окружения и снастей. Дождь, доступ к небу, Приманка и все последующие стадии рыбалки продолжают влиять как в ванилле.

Оператор может временно вернуть ванильную рыбалку для текущей сессии мира или сервера:

```text
/worstluck fishing vanilla
```

Дополнительные команды:

- `/worstluck fishing` — показать текущий режим рыбалки;
- `/worstluck fishing boots` — немедленно снова включить гарантированные кожаные ботинки.

Настройка не сохраняется. После закрытия и повторного открытия одиночного мира или перезапуска выделенного сервера гарантированные ботинки включаются снова. На выделенном сервере простое переподключение игрока не перезапускает серверную сессию. Для команд требуются права оператора или ведущего игры.

### Гарантированные атаки фантомов

Если игрок не спал три игровых дня, атаки фантомов гарантированно происходят каждые **1200–1400 тиков** при выполнении условий, совместимых с ванильной логикой:

- игрок находится на высоте Y 64 или выше;
- над игроком видно небо;
- освещение достаточно низкое для ванильного спавнера фантомов;
- спавн фантомов и враждебных мобов разрешён;
- игрок не находится в режиме наблюдателя;
- выбранная позиция для спавна свободна.

Для каждого подходящего игрока появляется стая из **четырёх фантомов** на высоте **20–34 блока над игроком** и с горизонтальным смещением до **9 блоков**. Условия проверяются независимо для каждого игрока.

### Детерминированные худшие сделки жителей

Новые предложения жителей теперь генерируются из детерминированных наборов худших сделок. Пары предметов выбраны по фактическим таблицам Minecraft 1.21.11, цены случайно зачарованной экипировки зафиксированы на ванильных максимумах, а зачарования всегда равны: Огнеупорность I для брони, Бич членистоногих I для оружия, Откидывание I для луков, Эффективность I для инструментов, Пронзающая стрела I для арбалетов и Проклятие утраты I для книг за 38 изумрудов и книгу.

Запрошенные наборы применены к бронникам, мясникам, картографам, священникам, фермерам, рыбакам, лучникам, кожевникам, библиотекарям, инструментальщикам, пастухам, каменщикам и оружейникам. Под чёрной и серой керамикой каменщика используются обычные чёрная и серая терракота; лучник продаёт стрелы длительной невидимости. Не указанные уровни остаются ванильными.

Важные отличия реальной версии 1.21.11 от старых описаний торговли:

- на третьем уровне картографа карта океана конкурирует с картой камер испытаний; мод исключает последнюю;
- на втором уровне картографа есть зависящие от биома и типа карты деревень, болот и джунглей, поэтому не все остальные его сделки фиксированы;
- на третьем уровне рыбака остаётся случайно зачарованная удочка;
- на четвёртом уровне библиотекаря также были часы; мод исключает их в пользу выбранного компаса.

Уже сохранённые сделки существующих жителей не переписываются: принудительная миграция сбросила бы количество использований, спрос и скидки и могла бы уничтожить предложения предыдущих уровней. Для полного эффекта нужны новые жители либо жители, у которых предложения соответствующего уровня ещё не были сгенерированы.

### Странствующие торговцы

Автоматический спавн странствующего торговца и его лам отключён. Уже существующие торговцы, команда `/summon` и яйца призыва не затрагиваются.

### Современная реализация

- Миксины и маппинги перенесены на Minecraft 1.21.11/Yarn.
- Проект обновлён до Java 21 и современного набора инструментов Fabric.
- Добавлен безопасный для мультиплеера расчёт расстояния до ближайшего игрока.
- Добавлены автоматические сборки GitHub Actions и дымовые тесты с запуском выделенного сервера.

## Сборка

Проект использует Gradle и Fabric Loom. CI собирает мод и сохраняет ремапнутый файл по адресу:

```text
dist/worst-luck-possible-2.1.0.jar
```

Обычным пользователям следует скачивать опубликованные сборки со [страницы Releases](https://github.com/myBSnotE/worst-luck-possible-port/releases/latest), а не из каталога `dist` репозитория.

Workflow также запускает временный выделенный Fabric-сервер, чтобы обнаруживать ошибки применения миксинов и сбои при запуске.

## Отказ от ответственности

Это неофициальная модернизация оригинального мода. Minecraft является товарным знаком Mojang Studios. Проект не связан с Mojang Studios и не одобрен компанией.

</details>
