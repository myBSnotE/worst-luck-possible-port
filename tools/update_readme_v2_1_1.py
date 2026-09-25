from pathlib import Path

path = Path("README.md")
text = path.read_text()

eng_anchor = "Operators can use `/worstluck lightning reduced` to change each ticking chunk from one lightning attempt every tick to a **5% chance per tick** — an average 20× reduction. `/worstluck lightning full` restores the original every-tick behavior, and `/worstluck lightning` shows the current mode. The setting is temporary and resets to full intensity when the world is reopened or the dedicated server restarts."
eng_add = eng_anchor + "\n\nSkeleton-horse traps are now created only when the final lightning position is in the same chunk as a living non-spectator player and that player is within **10 blocks** of the strike. When those conditions are met, the trap spawn is guaranteed instead of using local-difficulty randomness. The creating lightning bolt remains cosmetic, so it cannot kill the trap horse; the nearby player activates the rider ambush on the following tick. `doMobSpawning` and lightning rods are still respected."
text = text.replace(eng_anchor, eng_add, 1)

rus_anchor = "Оператор может использовать `/worstluck lightning reduced`, чтобы заменить одну попытку удара в каждом тикающем чанке каждый тик на **5%-й шанс каждый тик** — в среднем это снижение частоты в 20 раз. `/worstluck lightning full` возвращает исходный режим с попыткой каждый тик, а `/worstluck lightning` показывает текущий режим. Настройка временная и сбрасывается на полную интенсивность после повторного открытия мира или перезапуска выделенного сервера."
rus_add = rus_anchor + "\n\nЛошадь-ловушка теперь создаётся только тогда, когда конечная точка удара молнии находится в том же чанке, что и живой игрок не в режиме наблюдателя, а сам игрок находится не дальше **10 блоков** от удара. При выполнении условий ловушка появляется гарантированно, без случайности локальной сложности. Создавшая её молния остаётся декоративной и не может убить лошадь; находящийся рядом игрок активирует появление всадников на следующем тике. Правило `doMobSpawning` и громоотводы по-прежнему учитываются."
text = text.replace(rus_anchor, rus_add, 1)

text = text.replace("dist/worst-luck-possible-2.1.0.jar", "dist/worst-luck-possible-2.1.1.jar")
path.write_text(text)
