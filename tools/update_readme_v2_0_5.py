from pathlib import Path

path = Path("README.md")
text = path.read_text()

text = text.replace(
    "- Thunderstorms begin frequently, with lightning attempts across loaded chunks.",
    "- Thunderstorms begin frequently, with lightning attempts across loaded chunks. Operators can temporarily reduce those attempts by 20× for the current server session.",
)
text = text.replace(
    "- Грозы начинаются часто, а в загруженных чанках регулярно предпринимаются попытки удара молнии.",
    "- Грозы начинаются часто, а в загруженных чанках регулярно предпринимаются попытки удара молнии. Оператор может временно снизить частоту этих попыток в 20 раз для текущей серверной сессии.",
)
text = text.replace(
    "### Worst-luck fishing and temporary debug override",
    "### Lightning-rate control\n\nOperators can use `/worstluck lightning reduced` to change each ticking chunk from one lightning attempt every tick to a **5% chance per tick** — an average 20× reduction. `/worstluck lightning full` restores the original every-tick behavior, and `/worstluck lightning` shows the current mode. The setting is temporary and resets to full intensity when the world is reopened or the dedicated server restarts.\n\n### Worst-luck fishing and temporary debug override",
)
text = text.replace(
    "### Худшая рыбалка и временное отладочное отключение",
    "### Управление частотой молний\n\nОператор может использовать `/worstluck lightning reduced`, чтобы заменить одну попытку удара в каждом тикающем чанке каждый тик на **5%-й шанс каждый тик** — в среднем это снижение частоты в 20 раз. `/worstluck lightning full` возвращает исходный режим с попыткой каждый тик, а `/worstluck lightning` показывает текущий режим. Настройка временная и сбрасывается на полную интенсивность после повторного открытия мира или перезапуска выделенного сервера.\n\n### Худшая рыбалка и временное отладочное отключение",
)
text = text.replace(
    "dist/worst-luck-possible-2.0.4.jar",
    "dist/worst-luck-possible-2.0.5.jar",
)
path.write_text(text)
