# Worst Luck Possible 2.1.1

## Русский

- Лошади-ловушки появляются только при ударе молнии в чанк игрока и на расстоянии не более 10 блоков от живого игрока не в режиме наблюдателя.
- При выполнении условий спавн гарантирован и больше не зависит от локальной сложности.
- Создающая ловушку молния декоративная и не наносит лошади урон.
- Благодаря радиусу в 10 блоков ловушка активирует скелетов-всадников на следующем тике.
- `doMobSpawning` и громоотводы продолжают учитываться.

## English

- Skeleton-horse traps spawn only when lightning strikes a player's chunk within 10 blocks of a living non-spectator player.
- Once eligible, the trap spawn is guaranteed and no longer depends on local-difficulty randomness.
- The creating lightning is cosmetic and cannot damage the trap horse.
- The 10-block radius triggers the skeleton riders on the following tick.
- `doMobSpawning` and lightning rods remain respected.
