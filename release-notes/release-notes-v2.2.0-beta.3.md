# Worst Luck Possible 2.2.0-beta.3

> **Prerelease / предварительная версия.** Исправление повторного пополнения рейда ведьмами.

## Русский

- Исправлена причина, из-за которой один неудачный пакет поиска навсегда отключал появление новых ведьм до конца текущей волны.
- После успешного появления ведьмы полностью сбрасываются счётчик неудач и разрешение на завершение волны.
- Если в волне остаётся один или два налётчика, неудачный пакет из 64 точек повторяется через 20 тиков. Поэтому механизм продолжает работать после убийства каждой новой ведьмы.
- Если налётчиков не осталось совсем, выполняются пять независимых пакетов по 64 точки с интервалом в один тик. Только после провала всех 320 кандидатов ванильная логика получает возможность закончить волну.
- Временное отсутствие подходящего игрока больше не отключает пополнение навсегда: проверка повторяется через секунду.
- Моб-кап на эту механику не влияет, поскольку ведьмы являются участниками рейда и создаются как событийные сущности.

## English

- Fixed a failed candidate batch permanently disabling further witch refills for the current wave.
- Every successful witch spawn now fully resets failure and wave-finish state.
- With one or two raiders alive, a failed 64-position batch retries after 20 ticks, so the mechanic continues after every newly spawned witch is killed.
- With zero raiders, five independent 64-position batches run one tick apart. Vanilla may finish the wave only after all 320 candidates fail.
- Temporarily having no eligible nearby player no longer disables refills for the rest of the wave; the check retries after one second.
- The normal mob cap does not govern these witches because they are event-spawned raid members.
