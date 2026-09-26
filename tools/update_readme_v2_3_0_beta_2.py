from pathlib import Path
p = Path('README.md')
s = p.read_text(encoding='utf-8')
s = s.replace('- Projectiles receive strongly randomized inaccuracy.', '- Player-fired projectiles choose the largest angular deviation permitted by vanilla projectile uncertainty; the former arbitrary ±180° yaw/pitch rotation has been removed.')
s = s.replace('- Снаряды получают сильно рандомизированную неточность.', '- Снаряды игрока выбирают наибольшее угловое отклонение, допускаемое ванильной формулой неточности; прежний произвольный поворот на ±180° по горизонтали и вертикали удалён.')
s = s.replace('dist/worst-luck-possible-2.3.0-beta.1.jar', 'dist/worst-luck-possible-2.3.0-beta.2.jar')
p.write_text(s, encoding='utf-8')
