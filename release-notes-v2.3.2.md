# Worst Luck Possible 2.3.2

Stable release for Minecraft **1.21.11**, Fabric Loader **0.19.5+**, and Java **21**. Fabric API is not required.

## Highlights

- Fueled fire is held at age 0 and persists until it is explicitly extinguished.
- Vanilla's random direct-burn roll no longer consumes nearby fuel. When the fire is extinguished, all six adjacent flammable blocks are consumed in the same tick; adjacent TNT is primed first.
- Fire spread now runs before vanilla's early exits, can bridge onto solid non-flammable supports when nearby fuel sustains the target, and still uses the strict budget of eight additional ignitions per source tick.
- Each random-ticking lava block can produce up to eight guaranteed ignitions inside the area reachable by vanilla lava-fire logic. Valid targets above solid non-flammable floors are prioritized.
- Fixed the fire-age mixin signature that was caught by the dedicated-server smoke test.
- Replaced deprecated build and chunk-loading APIs. The clean build now completes without compiler or Gradle deprecation warnings.

## Verification

- Clean Gradle build: passed.
- Fabric dedicated-server startup with the 2.3.2 JAR: passed.
- All required mixins applied; the server reached `Done`, stopped normally, and saved all dimensions.

## Русский

Стабильный релиз для Minecraft **1.21.11**, Fabric Loader **0.19.5+** и Java **21**. Fabric API не требуется.

- Огонь рядом с топливом удерживается на возрасте 0 и горит, пока его явно не потушат.
- Случайная ванильная попытка больше не уничтожает соседнее топливо. При тушении огня все шесть соседних горючих блоков уничтожаются в тот же тик; соседний TNT перед этим активируется.
- Дополнительное распространение огня выполняется до ванильных ранних выходов и может переходить на плотный негорючий пол, если рядом есть топливо. Строгий лимит остаётся равным восьми дополнительным поджогам на тик источника.
- Каждый случайный тик лавы создаёт до восьми гарантированных очагов в пределах области, достижимой ванильной логикой поджигания. Приоритет получают подходящие точки над плотным негорючим полом.
- Исправлена сигнатура миксина возраста огня, обнаруженная серверным смоук-тестом.
- Устаревшие API сборки и проверки чанков заменены. Чистая сборка проходит без предупреждений компилятора и Gradle о deprecated API.

Чистая сборка Gradle и запуск выделенного Fabric-сервера успешно проверены. Все обязательные миксины применились; сервер дошёл до `Done`, штатно остановился и сохранил все измерения.
