package breakbadhabits.foundation.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.toMillis(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = toInstant(timeZone).toEpochMilliseconds()

fun Long.millisToLocalDateTime(
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)

fun millisDistanceBetween(
    range: ClosedRange<LocalDateTime>,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) = range.endInclusive.toMillis(timeZone) - range.start.toMillis(timeZone)
