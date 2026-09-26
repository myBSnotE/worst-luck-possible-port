# Worst Luck Possible 2.4.0-beta.3

Development notes for Minecraft **1.21.11**, Fabric Loader **0.19.5+**, Fabric API **0.141.6+**, and Java **21**.

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

### Исправление целей молний

- Выбор горючих целей теперь требует полного блока с твёрдой верхней гранью. Двери, люки, таблички, кровати и другие тонкие или составные блоки больше не выбираются.
- В `fabric.mod.json` добавлены ссылки на репозиторий и документацию.

### Расширение настроек

- Экран настроек разделён на страницы «Основные» и «Бой и взрывы».
- Добавлены профили «Ваниль», «Максимальная неудача» и автоматически определяемый «Пользовательский».
- Добавлены отдельные параметры разброса снарядов игрока, упреждения вражеских снарядов, критического урона снарядов и разрушительности взрывов.
- Профили одновременно меняют все настраиваемые игровые механики, но намеренно не затрагивают «Ответственный режим».
- Конфигурации миров beta.2 автоматически переходят на формат 2 и получают худшие значения по умолчанию для новых параметров.
