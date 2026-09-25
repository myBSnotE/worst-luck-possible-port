from pathlib import Path

path = Path("README.md")
text = path.read_text(encoding="utf-8")
text = text.replace("2.2.0-beta.1", "2.2.0-beta.2")

english_raid = "- Raid bonus rolls always choose their vanilla maximum. After each wave, one additional witch can join only when a distant non-persistent hostile can be replaced one-for-one. Raid members and protected mobs are never selected, so the swap does not inflate the entity count."
english_raid_new = english_raid + "\n- While an active wave has two or fewer raiders, the mod attempts to refill it with one witch at a time. Each batch checks up to 64 locations within 32 blocks of a nearby player, requiring low light (7 or less), a solid floor, two free blocks, loaded/ticking terrain, vanilla witch spawn validation, and proximity to the occupied village. A successful refill raises the wave above the low-raider threshold; killing another raider starts another batch. The wave may finish only after all 64 candidates in a batch fail."
text = text.replace(english_raid, english_raid_new)

russian_raid = "- Бонусный состав рейда всегда получает ванильный максимум. После каждой волны может присоединиться одна дополнительная ведьма, но только при возможности заменить дальнего непостоянного врага один к одному. Участники рейда и защищённые мобы не удаляются, поэтому общее число сущностей не растёт."
russian_raid_new = russian_raid + "\n- Пока в активной волне осталось не более двух налётчиков, мод пытается пополнять её одной ведьмой. Каждый пакет проверяет до 64 точек в радиусе 32 блоков от ближайшего игрока: освещение не выше 7, плотный пол, два свободных блока, загруженная и тикающая область, ванильная проверка спавна ведьмы и близость к занятой деревне. Успешный спавн поднимает число участников выше порога; после убийства следующего налётчика начинается новый пакет. Волна может завершиться только после провала всех 64 кандидатов одного пакета."
text = text.replace(russian_raid, russian_raid_new)

english_trap = "Skeleton-horse traps are now created only when the final lightning position is in the same chunk as a living non-spectator player and that player is within **10 blocks** of the strike. When those conditions are met, the trap spawn is guaranteed instead of using local-difficulty randomness. The creating lightning bolt remains cosmetic, so it cannot kill the trap horse; the nearby player activates the rider ambush on the following tick. `doMobSpawning` and lightning rods are still respected."
english_trap_new = english_trap + " New traps are suppressed when that player already has **96 or more living persistent/non-despawning mobs within 128 loaded blocks**. The lightning strike itself still occurs normally; only the additional trap group is skipped."
text = text.replace(english_trap, english_trap_new)

russian_trap = "Лошадь-ловушка теперь создаётся только тогда, когда конечная точка удара молнии находится в том же чанке, что и живой игрок не в режиме наблюдателя, а сам игрок находится не дальше **10 блоков** от удара. При выполнении условий ловушка появляется гарантированно, без случайности локальной сложности. Создавшая её молния остаётся декоративной и не может убить лошадь; находящийся рядом игрок активирует появление всадников на следующем тике. Правило `doMobSpawning` и громоотводы по-прежнему учитываются."
russian_trap_new = russian_trap + " Новая ловушка не создаётся, если в загруженной области радиусом 128 блоков вокруг этого игрока уже есть **96 или больше живых персистирующих либо недеспавнящихся мобов**. Сама молния при этом ударяет как обычно — пропускается только дополнительная группа ловушки."
text = text.replace(russian_trap, russian_trap_new)

path.write_text(text, encoding="utf-8")
