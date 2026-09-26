# Worst Luck Possible 2.3.3

Patch release for Minecraft **1.21.11**, Fabric Loader **0.19.5+**, and Java **21**. Fabric API is not required.

## Fix

- Fixed fueled fire not remaining at age 0.
- Vanilla calculates and stores a new age after reading the block-state argument, so resetting only that argument still allowed age 1 to be written. The source-fire write is now clamped as well.
- Existing fueled fire is reset to age 0 on its next scheduled tick and remains there on subsequent ticks.

## Verification

- Clean Gradle build: passed without warnings.
- Dedicated Fabric server: reached `Done` and stopped normally.
- Functional server test: a fueled source created at age 15 was read back as age 0 after repeated scheduled ticks.

## Русский

Патч-релиз для Minecraft **1.21.11**, Fabric Loader **0.19.5+** и Java **21**. Fabric API не требуется.

- Исправлено удержание огня рядом с топливом на возрасте 0.
- Ваниль вычисляет и сохраняет новый возраст уже после чтения аргумента состояния блока, поэтому прежняя замена аргумента всё ещё позволяла записать возраст 1. Теперь перехватывается и итоговая запись состояния источника.
- Уже существующий огонь рядом с топливом сбрасывается до возраста 0 на следующем запланированном тике и остаётся на нём далее.

Чистая сборка, запуск выделенного сервера и функциональная проверка перехода с возраста 15 на возраст 0 прошли успешно.
