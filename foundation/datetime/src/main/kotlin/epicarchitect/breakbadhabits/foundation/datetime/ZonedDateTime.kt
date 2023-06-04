package epicarchitect.breakbadhabits.foundation.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class ZonedDateTime(
    val dateTime: LocalDateTime,
    val timeZone: TimeZone
) : Comparable<ZonedDateTime> {
    val instant by lazy { dateTime.toInstant(timeZone) }

    override fun compareTo(other: ZonedDateTime) = dateTime.compareTo(other.dateTime)
}

data class ZonedDate(
    val date: LocalDate,
    val timeZone: TimeZone
) : Comparable<ZonedDate> {
    val instant by lazy {
        date.atStartOfDayIn(timeZone)
    }

    override fun compareTo(other: ZonedDate) = date.compareTo(other.date)
}

fun ZonedDateTime(
    instant: Instant,
    timeZone: TimeZone
) = ZonedDateTime(
    dateTime = instant.toLocalDateTime(timeZone),
    timeZone = timeZone
)

fun ZonedDate(
    instant: Instant,
    timeZone: TimeZone
) = ZonedDate(
    date = instant.toLocalDateTime(timeZone).date,
    timeZone = timeZone
)