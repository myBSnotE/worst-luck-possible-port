from pathlib import Path

path = Path("README.md")
text = path.read_text()

text = text.replace(
    "By default, every successful fishing catch yields exactly **one pair of leather boots** instead of using the fishing loot table.",
    "By default, every successful fishing catch yields exactly **one pair of leather boots with zero remaining durability** instead of using the fishing loot table.",
)
text = text.replace(
    "По умолчанию каждый успешный улов гарантированно даёт ровно **одну пару кожаных ботинок** вместо использования таблицы рыболовного лута.",
    "По умолчанию каждый успешный улов гарантированно даёт ровно **одну пару кожаных ботинок с нулевой оставшейся прочностью** вместо использования таблицы рыболовного лута.",
)
text = text.replace(
    "dist/worst-luck-possible-2.0.2.jar",
    "dist/worst-luck-possible-2.0.3.jar",
)
path.write_text(text)
