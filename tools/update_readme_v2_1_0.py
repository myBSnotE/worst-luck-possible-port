from pathlib import Path

path = Path("README.md")
text = path.read_text()

english = r'''### Deterministic worst villager trades

Newly generated villager offers now use deterministic worst-case pools. The selected item pairs follow the real Minecraft 1.21.11 trade tables, prices of randomly enchanted equipment are fixed at their vanilla maxima, and enchantments are fixed to Fire Protection I (armor), Bane of Arthropods I (weapons), Punch I (bows), Efficiency I (tools), Piercing I (crossbows), and Curse of Vanishing I enchanted books for 38 emeralds plus a book.

The requested profession/level choices are applied to armorers, butchers, cartographers, clerics, farmers, fishermen, fletchers, leatherworkers, librarians, toolsmiths, shepherds, masons, and weaponsmiths. The mason's black/gray ceramics are ordinary black and gray terracotta; fletchers sell long-invisibility tipped arrows. Pools not explicitly changed remain vanilla.

Important 1.21.11 differences from older trade descriptions:

- cartographer level 3 also contains a Trial Chambers map candidate, which is now excluded in favor of the ocean explorer map;
- cartographer level 2 has biome/type-aware village, swamp and jungle maps, so not every other cartographer offer is fixed;
- fisherman level 3 still has a randomly enchanted fishing rod;
- librarian level 4 also had a clock candidate, which is excluded by the requested compass choice.

Trades already stored on existing villagers are not rewritten, because doing so would reset uses, demand and discounts and could destroy earlier-level offers. Use new villagers, or villagers whose affected level has not generated its offers yet, for the complete effect.

### Wandering traders

Automatic wandering-trader spawning (including its trader llamas) is disabled. Existing traders, `/summon`, and spawn eggs are not affected.

'''

russian = r'''### Детерминированные худшие сделки жителей

Новые предложения жителей теперь генерируются из детерминированных наборов худших сделок. Пары предметов выбраны по фактическим таблицам Minecraft 1.21.11, цены случайно зачарованной экипировки зафиксированы на ванильных максимумах, а зачарования всегда равны: Огнеупорность I для брони, Бич членистоногих I для оружия, Откидывание I для луков, Эффективность I для инструментов, Пронзающая стрела I для арбалетов и Проклятие утраты I для книг за 38 изумрудов и книгу.

Запрошенные наборы применены к бронникам, мясникам, картографам, священникам, фермерам, рыбакам, лучникам, кожевникам, библиотекарям, инструментальщикам, пастухам, каменщикам и оружейникам. Под чёрной и серой керамикой каменщика используются обычные чёрная и серая терракота; лучник продаёт стрелы длительной невидимости. Не указанные уровни остаются ванильными.

Важные отличия реальной версии 1.21.11 от старых описаний торговли:

- на третьем уровне картографа карта океана конкурирует с картой камер испытаний; мод исключает последнюю;
- на втором уровне картографа есть зависящие от биома и типа карты деревень, болот и джунглей, поэтому не все остальные его сделки фиксированы;
- на третьем уровне рыбака остаётся случайно зачарованная удочка;
- на четвёртом уровне библиотекаря также были часы; мод исключает их в пользу выбранного компаса.

Уже сохранённые сделки существующих жителей не переписываются: принудительная миграция сбросила бы количество использований, спрос и скидки и могла бы уничтожить предложения предыдущих уровней. Для полного эффекта нужны новые жители либо жители, у которых предложения соответствующего уровня ещё не были сгенерированы.

### Странствующие торговцы

Автоматический спавн странствующего торговца и его лам отключён. Уже существующие торговцы, команда `/summon` и яйца призыва не затрагиваются.

'''

eng_anchor = "### Modernized implementation\n"
rus_anchor = "### Современная реализация\n"
if english not in text:
    text = text.replace(eng_anchor, english + eng_anchor, 1)
if russian not in text:
    text = text.replace(rus_anchor, russian + rus_anchor, 1)

eng_fishing = "By default, every successful fishing catch yields exactly **one pair of leather boots with zero remaining durability** instead of using the fishing loot table. This removes fishing as an unseeded source of favorable randomness and prevents it from supplying fish, treasure, enchanted books or leather through ordinary fishing loot."
eng_fishing_new = eng_fishing + "\n\nThe initial random bite-wait roll is always its maximum vanilla value: **600 ticks (30 seconds)** before environmental and equipment modifiers. Rain, sky access, Lure and all later fishing phases retain their normal influence."
text = text.replace(eng_fishing, eng_fishing_new, 1)

rus_fishing = "По умолчанию каждый успешный улов гарантированно даёт ровно **одну пару кожаных ботинок с нулевой оставшейся прочностью** вместо использования таблицы рыболовного лута. Это убирает рыбалку как несидированный источник благоприятной случайности и не позволяет получать через обычный рыболовный лут рыбу, сокровища, зачарованные книги или кожу."
rus_fishing_new = rus_fishing + "\n\nНачальная случайная длительность ожидания клёва всегда получает максимальное ванильное значение: **600 тиков (30 секунд)** до применения модификаторов окружения и снастей. Дождь, доступ к небу, Приманка и все последующие стадии рыбалки продолжают влиять как в ванилле."
text = text.replace(rus_fishing, rus_fishing_new, 1)
text = text.replace("dist/worst-luck-possible-2.0.5.jar", "dist/worst-luck-possible-2.1.0.jar")
path.write_text(text)
