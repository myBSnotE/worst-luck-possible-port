from pathlib import Path
p = Path('README.md')
s = p.read_text(encoding='utf-8')
needle = '- Armor worn by mobs takes no durability damage, including direct sunlight wear on skeleton and zombie helmets. The protection belongs to the wearer logic rather than the item component, so dropped armor is still ordinarily damageable.\n'
replacement = needle + '- Naturally generated mob equipment still never drops on death, but foreign items picked up from the ground retain vanilla guaranteed-drop ownership and are returned unchanged when the mob dies.\n'
s = s.replace(needle, replacement)
needle = '- Броня, надетая на мобов, не теряет прочность, включая прямой износ шлемов скелетов и зомби на солнце. Защита привязана к логике владельца, а не к компоненту предмета, поэтому выпавшая броня снова имеет обычную разрушаемость.\n'
replacement = needle + '- Естественно созданная экипировка мобов по-прежнему не выпадает после смерти, но подобранные с земли чужие предметы сохраняют ванильное гарантированное выпадение и возвращаются без изменений.\n'
s = s.replace(needle, replacement)
s = s.replace('dist/worst-luck-possible-2.3.0-beta.3.jar', 'dist/worst-luck-possible-2.3.0-beta.4.jar')
p.write_text(s, encoding='utf-8')
