# Worst Luck Possible 2.3.1

Stable release for Minecraft **1.21.11** / Fabric Loader **0.19.5+**. Fabric API is not required.

## Highlights

- Fire now forces destructive spread within the vanilla local spread volume, with a hard budget of eight additional block changes per source fire tick.
- Fire prioritizes appearing at the feet of nearby players and passive mobs standing on solid non-flammable blocks.
- Regular and ominous trial spawners always eject one baked potato as their post-combat reward; vaults remain unchanged.
- Every eligible non-creative anvil use damages the anvil or destroys its final state.
- Tamed cats no longer provide morning gifts.
- Added operator diagnostic command `/worstluck debug spawning`.
- Fixed player projectiles converging on one point when the camera did not move: worst-boundary spread now uses a varied random azimuth and preserves projectile speed.

The fire extension respects the world's fire-spread rule, requires the source fire to survive its vanilla tick, stays inside vanilla's direct/extended spread area, and uses a strict per-source budget to prevent unbounded work.

## Русский

Стабильный релиз для Minecraft **1.21.11** и Fabric Loader **0.19.5+**. Fabric API не требуется.

- Огонь гарантированно и разрушительно распространяется в локальных ванильных пределах, но не более чем на восемь дополнительных блоков за тик одного источника.
- Приоритет получают блоки у ног ближайших игроков и мирных животных, даже если они стоят на плотном негорючем блоке.
- Обычные и зловещие рассадники испытаний после боя всегда выбрасывают одну печёную картофелину; хранилища не изменены.
- Каждое подходящее использование наковальни не в творческом режиме повреждает её или уничтожает последнюю стадию.
- Приручённые кошки больше не приносят утренние подарки.
- Добавлена операторская команда `/worstluck debug spawning`.
- Исправлен полёт снарядов игрока в одну точку при неподвижной камере: максимальный граничный разброс теперь получает случайное направление и сохраняет скорость снаряда.

Сборка и запуск выделенного сервера успешно проверены GitHub Actions.
