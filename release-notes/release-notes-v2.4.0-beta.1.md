# Worst Luck Possible 2.4.0-beta.1

Prerelease for Minecraft **1.21.11**, Fabric Loader **0.19.5+**, and Java **21**. Fabric API is not required.

This update changes runtime-random outcomes only. Seed-derived systems and results already fixed by generated values remain untouched.

## Hostile projectiles

- Hostile shooters now use the projectile's existing vanilla uncertainty range to lead moving players.
- The target's accepted server-side movement is projected over the estimated flight time.
- If the exact intercept lies outside the vanilla uncertainty range, the closest permitted trajectory is selected.
- Projectile speed, gravity, drag, and in-flight direction are unchanged; this is launch-time spread selection, not homing.
- The random critical-projectile damage bonus is minimized for player-owned projectiles and maximized for hostile-mob projectiles.
- Dispenser projectile spread is unchanged.

## Explosions

- Explosion rays receive the maximum value from their vanilla runtime-random strength range.
- Explosions with vanilla `createFire` enabled ignite every valid affected position. Explosions without that flag still create no fire.
- Explosion-dependent item survival and stack-decay rolls fail whenever failure is possible. Guaranteed drops remain guaranteed.

## Foxes

- The vanilla 20% chance for a fox to spawn with an item is preserved.
- When that chance succeeds, the fox always receives an egg.

## Verification

- Clean Gradle build completed without warnings.
- Dedicated Fabric server reached `Done` and stopped normally.
- Mixin targets were checked against Minecraft 1.21.11 Yarn build.6 bytecode.

## Русский

Предварительный релиз для Minecraft **1.21.11**, Fabric Loader **0.19.5+** и Java **21**. Fabric API не требуется.

Обновление меняет только случайные исходы, вычисляемые во время игры. Системы, зависящие от сида или уже сгенерированных значений, не затронуты.

### Вражеские снаряды

- Враждебные стрелки используют существующий ванильный диапазон неточности, чтобы брать упреждение по движущемуся игроку.
- Положение цели прогнозируется по принятому сервером движению и оценочному времени полёта.
- Если точное упреждение не помещается в ванильный диапазон неточности, выбирается ближайшая допустимая траектория.
- Скорость, гравитация, сопротивление и направление снаряда в полёте не меняются: это выбор разброса при запуске, а не самонаведение.
- Случайная прибавка критического урона минимальна для снарядов игрока и максимальна для снарядов враждебных мобов.
- Разброс снарядов раздатчика не изменён.

### Взрывы

- Лучи взрыва получают максимальное значение из ванильного случайного диапазона силы.
- Взрывы с уже включённым ванильным флагом `createFire` поджигают каждую подходящую затронутую позицию. Остальные взрывы огонь не создают.
- Зависимые от взрыва броски сохранения предметов и уменьшения стаков заканчиваются неудачей, когда ваниль допускает потерю. Гарантированный лут остаётся гарантированным.

### Лисы

- Ванильный 20%-й шанс появления лисы с предметом сохранён.
- При успешном броске лиса всегда получает яйцо.

### Проверка

- Чистая Gradle-сборка завершилась без предупреждений.
- Выделенный Fabric-сервер дошёл до `Done` и штатно остановился.
- Точки внедрения миксинов сверены с байткодом Minecraft 1.21.11 Yarn build.6.
