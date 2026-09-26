# Worst Luck Possible — Minecraft 1.21.11 port

A modern Fabric port of **Worst Luck Possible**. Favorable randomness is replaced with the worst practical vanilla-compatible outcome: hostile spawns stay close, loot rolls low, mobs receive dangerous equipment, fire refuses to die, and useful random events become harmful.

> **Current release:** 2.3.3 · Minecraft 1.21.11 · Fabric Loader 0.19.5+ · Java 21 · Fabric API is not required

[Download the latest release](https://github.com/myBSnotE/worst-luck-possible-port/releases/latest) · [Report a bug](https://github.com/myBSnotE/worst-luck-possible-port/issues)

<details>
<summary><strong>English</strong></summary>

## Install

1. Install Fabric Loader for Minecraft **1.21.11**.
2. Download `worst-luck-possible-2.3.3.jar` from the [Releases page](https://github.com/myBSnotE/worst-luck-possible-port/releases/latest).
3. Put the JAR in the client or dedicated server `mods` folder.
4. Start Minecraft with Java **21**.

The port supports multiplayer and dedicated servers.

## Gameplay overview

### Fire, lava, and weather

- Fueled fire is kept at age 0 and no longer consumes adjacent fuel through vanilla's random direct-burn roll. It persists until extinguished.
- When ordinary fire is extinguished, all adjacent flammable blocks are consumed in the same tick; adjacent TNT is primed first.
- Each source-fire tick performs up to **eight** additional successful ignitions inside vanilla's local spread volume. It prioritizes fire at the feet of nearby players and passive mobs, including on solid non-flammable floors.
- Each random-ticking lava block can create up to **eight** fires in the volume reachable by vanilla lava ignition. Valid positions above solid non-flammable floors are tried first.
- Fire behavior still respects the world's fire-spread rule, loaded terrain, and strict per-source budgets.
- Thunderstorms are frequent. Lightning attempts can be temporarily reduced 20× with an operator command.
- Skeleton-horse traps are guaranteed only near a living player and are suppressed under extreme persistent-mob pressure.

### Loot, items, and progression

- Mob and block loot rolls choose the minimum result; gravel never drops flint.
- Piglin bartering gives two magma cream.
- Fishing gives one pair of leather boots with zero remaining durability, and the initial bite wait uses the vanilla maximum of 600 ticks.
- Trial spawners give one baked potato as their post-combat reward.
- Eyes of Ender always break; successful Ender Pearl teleports create an endermite when monster spawning is enabled.
- Unbreaking never prevents durability loss.
- Eggs never hatch chicks. Bonemealed crops grow by the minimum two stages; supported trees and fungi choose their existing failure outcome.
- Chorus fruit tries its vanilla-valid destinations from most dangerous to least dangerous.
- Every eligible non-creative anvil use advances the anvil's damage state.
- Tamed cats do not give morning gifts.
- New villager offers use deterministic worst-case pools. Existing stored offers are not rewritten.
- Automatic wandering-trader spawning is disabled; existing and manually summoned traders are unaffected.

### Hostile mobs and combat

- Zombies and variants are babies, leaders, and receive strong vanilla-compatible equipment. On Hard, zombies break doors and leaders call reinforcements until the local safety limit is reached.
- Every spider becomes a spider jockey; on Hard the spider has permanent Speed I.
- Naturally spawned slimes and magma cubes use the largest natural size. Slimes use the minimum jump delay; magma cubes retain their vanilla ×4 delay.
- Drowned are more common, carry tridents.
- Endermen teleport along the player's look direction. The Ender Dragon does not perch and keeps an unfavorable flight height.
- Shulkers use the minimum random bullet interval. Witches choose harmful potion outcomes under the normal condition order.
- Naturally generated horses and bred horse-family children receive the lowest supported attributes.
- Mob armor does not lose durability while worn. Naturally generated equipment does not drop, while foreign items picked up from the ground retain vanilla ownership and drop behavior.
- Mob equipment receives strong item-valid enchantments. Player projectiles receive varied spread at the boundary of vanilla's uncertainty range without changing projectile speed.

### Spawning, raids, and server safeguards

- Passive mobs do not spawn during ordinary natural-spawn cycles.
- Hostile packs are maximized and ordinary natural spawns prefer valid positions **24–32 blocks** from a nearby player.
- At the hostile mob cap, a distant eligible hostile is removed only after a valid closer replacement has actually spawned.
- Hostiles 32–128 blocks away are temporarily protected from random despawning when too few enemies are near their assigned player. Vanilla immediate despawning beyond 128 blocks is unchanged.
- Named, persistent, raid, and otherwise protected mobs are never selected for replacement. All density decisions use each mob's nearest player.
- After three sleepless in-game days, every eligible player receives four phantoms at guaranteed 1200–1400 tick intervals.
- Raid bonus rolls choose their vanilla maximum. Low active waves repeatedly try to add a witch at a valid dark village position without allowing unbounded entity growth.
- Density scans are cached once per second. Replacement and pathfinding work use adaptive per-tick budgets and emergency limits.

## Operator commands

| Command | Effect |
| --- | --- |
| `/worstluck lightning` | Show the current lightning mode. |
| `/worstluck lightning full` | Restore one lightning attempt per ticking chunk per tick. |
| `/worstluck lightning reduced` | Use a 5% attempt chance per ticking chunk per tick. |
| `/worstluck fishing` | Show the current fishing mode. |
| `/worstluck fishing boots` | Force ruined leather boots. |
| `/worstluck fishing vanilla` | Temporarily restore vanilla fishing. |
| `/worstluck debug spawning` | Show the invoking player's cached mob-pressure diagnostics. |

Command overrides are temporary and reset after reopening the world or restarting the server.

## Build and verification

The project uses Gradle, Fabric Loom, Yarn mappings, Java 21 bytecode, and Fabric Loader 0.19.5. GitHub Actions builds the remapped JAR and starts a temporary dedicated server to catch mixin and startup failures.

```text
gradle clean build
```

The CI artifact is written to `dist/worst-luck-possible-2.3.3.jar`. End users should download the published release asset instead of the repository copy.

## Original project and credits

- **Original creator:** Heppe
- **Original mod:** [Worst Luck Possible on Modrinth](https://modrinth.com/mod/worst-luck-possible)
- **Original video:** [Is it possible to beat Minecraft with only bad RNG?](https://www.youtube.com/watch?v=LYmyuoRJecA)
- **Original target:** Minecraft 1.16.1 with Fabric
- **Port contributor:** HyBri:D

The original compiled reference is preserved as [`dist/worst-luck-possible-1.0.0_original.jar`](dist/worst-luck-possible-1.0.0_original.jar). The Modrinth listing currently identifies the original project as MIT-licensed, while the embedded metadata in that JAR declares CC0-1.0. This port's metadata declares CC0-1.0.

</details>

<details open>
<summary><strong>Русский</strong></summary>

## Установка

1. Установите Fabric Loader для Minecraft **1.21.11**.
2. Скачайте `worst-luck-possible-2.3.3.jar` со [страницы Releases](https://github.com/myBSnotE/worst-luck-possible-port/releases/latest).
3. Поместите JAR в папку `mods` клиента или выделенного сервера.
4. Запустите Minecraft с Java **21**.

Порт поддерживает мультиплеер и выделенные серверы.

## Обзор механик

### Огонь, лава и погода

- Огонь рядом с топливом сохраняет возраст 0 и больше не уничтожает соседнее топливо случайным ванильным броском. Он горит, пока его не потушат.
- При тушении обычного огня все соседние горючие блоки уничтожаются в тот же тик; соседний TNT перед этим активируется.
- Каждый тик источника огня выполняется до **восьми** дополнительных успешных поджогов в локальном ванильном объёме. В первую очередь огонь появляется у ног ближайших игроков и мирных животных, в том числе на плотном негорючем полу.
- Каждый случайный тик лавы создаёт до **восьми** очагов в пределах ванильной области поджигания. Сначала выбираются подходящие позиции над плотным негорючим полом.
- Логика огня учитывает правило распространения огня, загрузку местности и строгий лимит работы на каждый источник.
- Грозы происходят часто. Оператор может временно снизить число попыток удара молнии в 20 раз.
- Лошади-ловушки гарантированно создаются только рядом с живым игроком и не появляются при чрезмерном количестве постоянных мобов.

### Лут, предметы и развитие

- Броски лута мобов и блоков выбирают минимальный результат; из гравия не выпадает кремень.
- Пиглины выдают два сгустка магмы.
- Рыбалка даёт одну пару полностью сломанных кожаных ботинок, а начальное ожидание клёва получает ванильный максимум в 600 тиков.
- Рассадники испытаний после боя выдают одну печёную картофелину.
- Око Края всегда ломается; успешная телепортация жемчугом создаёт эндермита, если разрешён спавн монстров.
- Прочность (Unbreaking) никогда не предотвращает износ.
- Из яиц не вылупляются цыплята. Культуры от костной муки растут на минимальные две стадии; поддерживаемые деревья и грибы выбирают существующий исход с неудачей.
- Плод хоруса перебирает ванильно допустимые точки от самой опасной к наименее опасной.
- Каждое подходящее использование наковальни не в творческом режиме переводит её на следующую стадию повреждения.
- Приручённые кошки не приносят утренние подарки.
- Новые сделки жителей создаются из детерминированных худших наборов. Уже сохранённые сделки не переписываются.
- Автоматический спавн странствующего торговца отключён; существующие и призванные вручную торговцы не затронуты.

### Враждебные мобы и бой

- Зомби и их варианты появляются детьми и лидерами с сильной ванильно допустимой экипировкой. На высокой сложности зомби ломают двери, а лидеры вызывают подкрепление до достижения локального безопасного предела.
- Каждый паук становится паучьим наездником; на высокой сложности паук получает постоянную Скорость I.
- Натурально заспавненные слизни и магмовые кубы имеют максимальный размер. Слизни используют минимальную задержку прыжка, а магмовые кубы сохраняют ванильный множитель ×4.
- Утопленники встречаются чаще, носят трезубцы.
- Эндермены телепортируются вдоль направления взгляда игрока. Дракон Края не садится и сохраняет невыгодную высоту полёта.
- Шалкеры используют минимальный случайный интервал выстрела. Ведьмы выбирают вредные зелья в рамках обычного порядка условий.
- Естественные лошади и потомки семейства лошадей получают минимальные поддерживаемые характеристики.
- Броня мобов не теряет прочность, пока надета. Естественная экипировка не выпадает, а подобранные с земли чужие предметы сохраняют ванильное владение и правила выпадения.
- Экипировка мобов получает сильные допустимые для предмета зачарования. Снаряды игрока отклоняются в случайном направлении до границы ванильной неточности без изменения скорости.

### Спавн, рейды и защита сервера

- Мирные мобы не появляются в обычных циклах естественного спавна.
- Размер враждебных стай максимален, а обычный естественный спавн предпочитает допустимые позиции в **24–32 блоках** от ближайшего игрока.
- При заполненном моб-капе дальний подходящий враг удаляется только после фактического успешного появления более близкой замены.
- Враги на расстоянии 32–128 блоков временно защищены от случайного деспавна, если рядом с закреплённым за ними игроком слишком мало противников. Мгновенный ванильный деспавн за 128 блоками не изменён.
- Именованные, постоянные, рейдовые и другие защищённые мобы не выбираются для замены. Плотность всегда считается относительно ближайшего к каждому мобу игрока.
- После трёх игровых дней без сна каждый подходящий игрок получает четырёх фантомов с гарантированным интервалом 1200–1400 тиков.
- Бонусные броски рейда выбирают ванильный максимум. Малочисленная активная волна повторно пытается добавить ведьму в допустимой тёмной точке деревни, не создавая неограниченного роста сущностей.
- Плотность кэшируется раз в секунду. Замена мобов и поиск пути используют адаптивные потиковые бюджеты и аварийные пределы.

## Команды оператора

| Команда | Действие |
| --- | --- |
| `/worstluck lightning` | Показать текущий режим молний. |
| `/worstluck lightning full` | Вернуть одну попытку удара на каждый тикающий чанк за тик. |
| `/worstluck lightning reduced` | Использовать 5%-ю вероятность попытки на тикающий чанк за тик. |
| `/worstluck fishing` | Показать текущий режим рыбалки. |
| `/worstluck fishing boots` | Принудительно включить сломанные кожаные ботинки. |
| `/worstluck fishing vanilla` | Временно вернуть ванильную рыбалку. |
| `/worstluck debug spawning` | Показать кэшированную диагностику давления мобов для вызвавшего игрока. |

Изменения команд не сохраняются и сбрасываются после повторного открытия мира или перезапуска сервера.

## Сборка и проверка

Проект использует Gradle, Fabric Loom, Yarn mappings, байткод Java 21 и Fabric Loader 0.19.5. GitHub Actions собирает ремапнутый JAR и запускает временный выделенный сервер, чтобы обнаружить ошибки миксинов и запуска.

```text
gradle clean build
```

CI сохраняет файл как `dist/worst-luck-possible-2.3.3.jar`. Обычным пользователям следует скачивать опубликованный файл релиза, а не копию из репозитория.

## Оригинальный проект и авторы

- **Автор оригинала:** Heppe
- **Оригинальный мод:** [Worst Luck Possible на Modrinth](https://modrinth.com/mod/worst-luck-possible)
- **Оригинальное видео:** [Is it possible to beat Minecraft with only bad RNG?](https://www.youtube.com/watch?v=LYmyuoRJecA)
- **Версия оригинала:** Minecraft 1.16.1 с Fabric
- **Автор порта:** HyBri:D

Оригинальный JAR, использованный как эталон поведения, сохранён в [`dist/worst-luck-possible-1.0.0_original.jar`](dist/worst-luck-possible-1.0.0_original.jar). На странице Modrinth сейчас указана лицензия MIT, а встроенные метаданные JAR указывают CC0-1.0. В метаданных этого порта указана CC0-1.0.

</details>

## Disclaimer / Отказ от ответственности

This is an unofficial modernization of the original mod. Minecraft is a trademark of Mojang Studios. This project is not affiliated with or endorsed by Mojang Studios.

Это неофициальная модернизация оригинального мода. Minecraft является товарным знаком Mojang Studios. Проект не связан с Mojang Studios и не одобрен компанией.
