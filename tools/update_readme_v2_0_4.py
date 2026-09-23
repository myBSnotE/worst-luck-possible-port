from pathlib import Path

path = Path("README.md")
text = path.read_text()

text = text.replace(
    "- Zombie leaders reliably call reinforcements when damaged on Hard.",
    "- Zombie leaders reliably call reinforcements when damaged on Hard, unless the relevant player already has at least 140 living mobs within 128 blocks.",
)
text = text.replace(
    "- Зомби-лидеры гарантированно вызывают подкрепление при получении урона на высокой сложности.",
    "- Зомби-лидеры гарантированно вызывают подкрепление при получении урона на высокой сложности, если только в радиусе 128 блоков от соответствующего игрока ещё нет 140 живых мобов.",
)
text = text.replace(
    "dist/worst-luck-possible-2.0.3.jar",
    "dist/worst-luck-possible-2.0.4.jar",
)
path.write_text(text)
