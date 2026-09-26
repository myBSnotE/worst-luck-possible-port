from pathlib import Path
p = Path('README.md')
s = p.read_text(encoding='utf-8')
s = s.replace('### Experimental 2.3.0 beta mechanics', '### Version 2.3.0 gameplay mechanics')
s = s.replace('Version **2.3.0** remains a prerelease because it changes several independent vanilla RNG paths and needs focused gameplay testing.', 'Version **2.3.0** promotes the tested beta mechanics to the stable release, including the projectile-spread, spear-enchantment and picked-up-equipment fixes.')
needle = '- Naturally generated mob equipment still never drops on death, but foreign items picked up from the ground retain vanilla guaranteed-drop ownership and are returned unchanged when the mob dies.\n'
s = s.replace(needle, needle + '- When a mob replaces naturally generated equipment with a better item, the old natural item never wins its vanilla 8.5% drop roll. Previously picked-up foreign equipment still returns normally when replaced.\n')
s = s.replace('### Экспериментальные механики беты 2.3.0', '### Игровые механики версии 2.3.0')
s = s.replace('Версия **2.3.0** пока остаётся предварительной: она меняет несколько независимых путей ванильной случайности и требует отдельного игрового тестирования.', 'Версия **2.3.0** переносит проверенные механики беты в стабильный релиз, включая исправления разброса снарядов, зачарований копий и возврата подобранных предметов.')
needle = '- Естественно созданная экипировка мобов по-прежнему не выпадает после смерти, но подобранные с земли чужие предметы сохраняют ванильное гарантированное выпадение и возвращаются без изменений.\n'
s = s.replace(needle, needle + '- Когда моб заменяет естественно созданную экипировку более предпочтительным предметом, старый естественный предмет всегда проигрывает ванильный 8,5%-й бросок и исчезает. Ранее подобранные чужие предметы при последующей замене возвращаются как обычно.\n')
s = s.replace('dist/worst-luck-possible-2.3.0-beta.4.jar', 'dist/worst-luck-possible-2.3.0.jar')
p.write_text(s, encoding='utf-8')
