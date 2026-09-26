# Worst Luck Possible 2.0.4

## English

- Zombie reinforcement calls are disabled whenever the relevant player already has **140 or more living mobs within 128 blocks**.
- Nearby-mob counts are cached for one second per player so mass-damage events such as frequent lightning do not turn the safety check itself into a lag source.
- Below the limit, the existing guaranteed reinforcement behavior on Hard is unchanged.

## Русский

- Призыв подкрепления зомби отключается, если в радиусе **128 блоков** от соответствующего игрока уже находится **140 или больше живых мобов**.
- Количество мобов кэшируется отдельно для каждого игрока на одну секунду, чтобы массовый урон, например частые молнии, не превращал саму проверку в источник лагов.
- Ниже лимита прежний гарантированный призыв подкрепления на высокой сложности работает без изменений.

## Requirements / Требования

- Minecraft **1.21.11**
- Fabric Loader **0.19.5** or newer / или новее
- Java **21**
