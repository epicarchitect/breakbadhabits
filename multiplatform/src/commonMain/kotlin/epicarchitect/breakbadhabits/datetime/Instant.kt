package epicarchitect.breakbadhabits.datetime

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

fun ClosedRange<Instant>.toLocalDateTimeRange(
    timeZone: TimeZone
) = start.toLocalDateTime(timeZone)..endInclusive.toLocalDateTime(timeZone)

fun ClosedRange<Instant>.duration() = endInclusive - start
