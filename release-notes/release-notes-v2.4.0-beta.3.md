# Worst Luck Possible 2.4.0-beta.3

Development notes for Minecraft **1.21.11**, Fabric Loader **0.19.5+**, Fabric API **0.141.6+**, and Java **21**.

## Looting and fire fixes

- Looting now adds exactly one item per enchantment level, subject to the loot function's existing cap. Vanilla actually uses `round(level × countProvider)`—commonly a random 0…level bonus—but the previous global minimum provider reduced it to zero every time.
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

### Исправления «Добычи» и огня

- «Добыча» теперь добавляет ровно один предмет за уровень с учётом существующего лимита функции лута. Ванилла на самом деле использует `round(уровень × числовой провайдер)` — обычно случайную прибавку 0…уровень, — но прежний общий выбор минимума всегда сводил её к нулю.
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
