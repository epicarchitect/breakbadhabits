package epicarchitect.breakbadhabits.foundation.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


data class ZonedDateTimeRange internal constructor(
    override val start: ZonedDateTime,
    override val endInclusive: ZonedDateTime
) : ClosedRange<ZonedDateTime>

data class ZonedDateRange internal constructor(
    override val start: ZonedDate,
    override val endInclusive: ZonedDate
) : ClosedRange<ZonedDate>

val ZonedDateTimeRange.timeZone get() = start.timeZone

val ZonedDateRange.timeZone get() = start.timeZone

fun ZonedDateTimeRange.toDateRange() = ZonedDateRange(
    start.dateTime.date,
    endInclusive.dateTime.date,
    timeZone
)

operator fun ZonedDateTime.rangeTo(end: ZonedDateTime) = ZonedDateTimeRange(this, end)

operator fun ZonedDate.rangeTo(end: ZonedDate) = ZonedDateRange(this, end)

fun ZonedDateTimeRangeOfOne(value: ZonedDateTime): ZonedDateTimeRange =
    ZonedDateTimeRange(value, value)

fun ZonedDateTimeRange(
    start: Instant,
    endInclusive: Instant,
    timeZone: TimeZone
): ZonedDateTimeRange = ZonedDateTimeRange(
    start.toLocalDateTime(timeZone),
    endInclusive.toLocalDateTime(timeZone),
    timeZone
)

fun ZonedDateTimeRange(
    start: LocalDateTime,
    endInclusive: LocalDateTime,
    timeZone: TimeZone
): ZonedDateTimeRange = ZonedDateTime(start, timeZone)..ZonedDateTime(endInclusive, timeZone)

fun ZonedDateRange(
    start: LocalDate,
    endInclusive: LocalDate,
    timeZone: TimeZone
): ZonedDateRange = ZonedDate(start, timeZone)..ZonedDate(endInclusive, timeZone)

