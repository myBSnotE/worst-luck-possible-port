# Worst Luck Possible 2.4.0-beta.2

Prerelease for Minecraft **1.21.11**, Fabric Loader **0.19.5+**, Fabric API **0.141.6+**, and Java **21**. Mod Menu is optional.

## Settings

- Added a bilingual settings screen with a hover tooltip for every value.
- Added optional Mod Menu integration.
- Added a configurable key binding, `L` by default, which also opens the screen without Mod Menu.
- Outside a world, the screen edits defaults for newly configured worlds.
- Inside a world, it edits a server-authoritative configuration stored with that world.
- Added responsible mode. A responsible-mode world can only be changed by an operator; ordinary single-player defaults cannot unlock it.
- Removed the fishing and lightning gameplay commands. `/worstluck debug spawning` remains.

## Configurable mechanics

- Thunderstorm frequency: Maximum or Vanilla.
- Lightning targets: loaded area; players and passive mobs; players, passive mobs, and flammable blocks; or Vanilla.
- Lightning attempt frequency: 1–100%.
- Fishing: Vanilla, Bad, or Long vanilla.
- Smart burning: Off, Eternal, or Accelerated.

## Fire fix

- Fire targeting at the feet of players and passive mobs now requires a positive vanilla burn chance at the exact target position.
- Fire can still appear over a nonflammable solid floor when nearby fuel makes that position valid, but it can no longer bridge across an entirely nonflammable surface.
- Restored Accelerated mode's visibly immediate 2.3.1 spread pass: direct flammable neighbors are forced first, followed by valid air positions, with a shared limit of eight placements.

## Client fix

- Fixed the settings screen requesting a second background blur during the same frame.
- Minecraft 1.21.11 already renders the screen background from `Screen#renderWithTooltip`; the duplicate call caused `IllegalStateException: Can only blur once per frame`, especially visible with screen-modifying clients such as SpruceUI.

## Documentation

- Added detailed documentation for configuration, permissions, responsible mode, weather, fire, lava, explosions, loot, progression, mobs, combat, spawning, raids, and the runtime-randomness boundary.
- Each mechanic distinguishes vanilla behavior, the mod's intervention, and the resulting gameplay behavior.

## Verification

- Clean Gradle build passed without compiler or Gradle deprecation warnings.
- Dedicated Fabric server with Fabric API reached `Done`, loaded and retained alternate world settings, and stopped normally.
- The removed fishing command was rejected while the debug command tree remained registered.
- Functional server tests confirmed both sides of the fix: accelerated fire does not appear at a cow's feet on a nonflammable floor without adjacent fuel, while a direct flammable neighbor is consumed during the accelerated post-tick pass.
- The settings render path was checked against Minecraft 1.21.11 bytecode to ensure the screen no longer calls `renderBackground` itself.

## Русский

Предварительный релиз для Minecraft **1.21.11**, Fabric Loader **0.19.5+**, Fabric API **0.141.6+** и Java **21**. Mod Menu необязателен.

### Настройки

- Добавлен двуязычный экран настроек со всплывающей подсказкой для каждого значения.
- Добавлена необязательная интеграция с Mod Menu.
- Добавлена переназначаемая клавиша открытия настроек — по умолчанию `L`.
- Вне мира экран меняет параметры новых миров, а внутри мира — серверную конфигурацию текущего мира.
- Добавлен ответственный режим: настройки такого мира может менять только оператор.
- Команды управления рыбалкой и молниями удалены. `/worstluck debug spawning` сохранена.

### Настраиваемые механики

- Частота гроз: максимальная или ванильная.
- Цели молний: прогруженная область; игроки и мирные мобы; игроки, мирные мобы и горючие блоки; ванилла.
- Частота попыток молнии: 1–100%.
- Рыбалка: ванильная, плохая или долгая ванильная.
- Умное горение: выкл., вечное или ускоренное.

### Исправление огня

- Поджигание у ног игроков и мирных мобов теперь требует положительного ванильного шанса горения в точной целевой позиции.
- Огонь всё ещё может появиться над негорючим полом при наличии подходящего соседнего топлива, но больше не перескакивает через полностью негорючую поверхность.
- В режим «Ускоренное» возвращён заметно мгновенный проход из 2.3.1: сначала принудительно обрабатываются прямые горючие соседи, затем допустимые воздушные точки; общий лимит — восемь установок.

### Исправление клиента

- Экран настроек больше не запрашивает повторное размытие фона в том же кадре.
- В Minecraft 1.21.11 фон уже отрисовывается через `Screen#renderWithTooltip`; повторный вызов приводил к `IllegalStateException: Can only blur once per frame`, особенно с модами интерфейса вроде SpruceUI.

### Документация и проверка

- Добавлена подробная документация механик, настроек, прав доступа и границ вмешательства в случайность.
- Чистая сборка и запуск выделенного сервера с Fabric API прошли успешно.
- Сервер сохранил альтернативную конфигурацию мира и отверг удалённую команду рыбалки.
- Функциональные серверные тесты подтвердили обе стороны исправления: ускоренный огонь не появляется у ног коровы на негорючем полу без соседнего топлива, а прямой горючий сосед уничтожается ускоренным проходом после тика.