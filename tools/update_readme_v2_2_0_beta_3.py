from pathlib import Path

path = Path("README.md")
text = path.read_text(encoding="utf-8")
text = text.replace("2.2.0-beta.2", "2.2.0-beta.3")

old_en = "- While an active wave has two or fewer raiders, the mod attempts to refill it with one witch at a time. Each batch checks up to 64 locations within 32 blocks of a nearby player, requiring low light (7 or less), a solid floor, two free blocks, loaded/ticking terrain, vanilla witch spawn validation, and proximity to the occupied village. A successful refill raises the wave above the low-raider threshold; killing another raider starts another batch. The wave may finish only after all 64 candidates in a batch fail."
new_en = "- While an active wave has two or fewer raiders, the mod keeps trying to refill it with one witch at a time. Each batch checks up to 64 locations within 32 blocks of a nearby player, requiring low light (7 or less), a solid floor, two free blocks, loaded/ticking terrain, vanilla witch spawn validation, and proximity to the occupied village. A successful refill resets all failure state, so killing the witch or another raider starts fresh attempts instead of permanently disabling the mechanic. Failed batches retry every second while any raider remains. With zero raiders, five independent 64-position batches are attempted before vanilla may finish the wave."
text = text.replace(old_en, new_en)

old_ru = "- Пока в активной волне осталось не более двух налётчиков, мод пытается пополнять её одной ведьмой. Каждый пакет проверяет до 64 точек в радиусе 32 блоков от ближайшего игрока: освещение не выше 7, плотный пол, два свободных блока, загруженная и тикающая область, ванильная проверка спавна ведьмы и близость к занятой деревне. Успешный спавн поднимает число участников выше порога; после убийства следующего налётчика начинается новый пакет. Волна может завершиться только после провала всех 64 кандидатов одного пакета."
new_ru = "- Пока в активной волне осталось не более двух налётчиков, мод постоянно пытается пополнять её одной ведьмой. Каждый пакет проверяет до 64 точек в радиусе 32 блоков от ближайшего игрока: освещение не выше 7, плотный пол, два свободных блока, загруженная и тикающая область, ванильная проверка спавна ведьмы и близость к занятой деревне. Успешный спавн полностью сбрасывает состояние неудач, поэтому убийство ведьмы или другого налётчика запускает новые попытки, а не отключает механику до конца волны. Пока остаётся хотя бы один налётчик, неудачные пакеты повторяются каждую секунду. При нуле налётчиков выполняются пять независимых пакетов по 64 позиции, и только после провала всех пяти ванильная логика может завершить волну."
text = text.replace(old_ru, new_ru)

path.write_text(text, encoding="utf-8")
