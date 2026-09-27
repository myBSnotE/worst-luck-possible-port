# Worst Luck Possible 2.4.0-beta.3

Development notes for Minecraft **1.21.11**, Fabric Loader **0.19.5+**, Fabric API **0.141.6+**, and Java **21**.

## Configurable mob Looting and spawning

- Replaced the temporary weapon-level Looting fix with a Mob Looting slider: Off, Looting I, II, or III.
- The default fixed effective Looting I bonus keeps blaze rods obtainable despite global minimum loot rolls.
- JSON field `mobLootingLevel` accepts any nonnegative integer, including values above the UI range for modpacks.
- Added a Spawning page for passive spawning, hostile distance, hostile intensity, distant-hostile replacement, and phantom behavior.
- Each spawning intervention now has a true vanilla bypass.
- Config format is now 3; existing worlds migrate with the safe Looting I default and the previous spawning behavior.

## Fire fixes

- Eternal and Accelerated fire, including lava ignition, now require the block directly below every new air fire to be flammable. The matching vanilla placement writes are constrained during enhanced ticks as well, preventing fire from returning over stone and other nonflammable supports.
- Fire mode Off remains fully vanilla.

## Lightning targeting fix

- Flammable-block targeting now requires a full block with a solid upper face. Doors, trapdoors, signs, beds, and other thin or multipart blocks are no longer selected.
- Added repository and documentation links to `fabric.mod.json`.

## Settings expansion

- Split the settings screen into General and Combat & explosions pages.
- Added Vanilla, Maximum bad luck, and automatically detected Custom profiles.
- Added independent controls for player projectile spread, hostile projectile leading, critical projectile damage, and explosion destruction.
- Profiles update every configurable gameplay mechanic together but deliberately leave Responsible mode unchanged.
- Existing beta.2 world configs migrate automatically to format 2 and retain worst-luck defaults for the newly configurable mechanics.

## Русский

### Настраиваемая «Добыча» мобов и спавн

- Временное исправление по уровню оружия заменено ползунком «Добыча мобов»: «Выкл.», «Добыча I», II или III.
- Стандартная фиксированная «Добыча I» сохраняет возможность получать огненные стержни при общем выборе минимального лута.
- Поле JSON `mobLootingLevel` принимает любое неотрицательное целое, включая уровни выше диапазона интерфейса для сборок.
- Добавлена страница «Спавн» с настройками мирных мобов, расстояния и интенсивности врагов, замены дальних врагов и фантомов.
- Каждая механика спавна получила настоящий ванильный режим.
- Формат конфигурации повышен до 3; существующие миры получают безопасную «Добычу I» и прежние параметры спавна.

### Исправления огня

- Вечный и ускоренный огонь, включая возгорание от лавы, теперь требует горючего блока прямо под каждой новой воздушной позицией. Соответствующие ванильные установки тоже ограничены во время усиленных тиков, поэтому огонь не возвращается над камнем и другой негорючей опорой.
- Режим «Выкл.» остаётся полностью ванильным.

### Исправление целей молний

- Выбор горючих целей теперь требует полного блока с твёрдой верхней гранью. Двери, люки, таблички, кровати и другие тонкие или составные блоки больше не выбираются.
- В `fabric.mod.json` добавлены ссылки на репозиторий и документацию.

### Расширение настроек

- Экран настроек разделён на страницы «Основные» и «Бой и взрывы».
- Добавлены профили «Ваниль», «Максимальная неудача» и автоматически определяемый «Пользовательский».
- Добавлены отдельные параметры разброса снарядов игрока, упреждения вражеских снарядов, критического урона снарядов и разрушительности взрывов.
- Профили одновременно меняют все настраиваемые игровые механики, но намеренно не затрагивают «Ответственный режим».
- Конфигурации миров beta.2 автоматически переходят на формат 2 и получают худшие значения по умолчанию для новых параметров.
