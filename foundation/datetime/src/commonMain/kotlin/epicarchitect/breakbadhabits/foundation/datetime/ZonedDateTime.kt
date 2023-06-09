package epicarchitect.breakbadhabits.foundation.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class ZonedDateTime(
    val instant: Instant,
    val timeZone: TimeZone
) : Comparable<ZonedDateTime> {
    val dateTime by lazy { instant.toLocalDateTime(timeZone) }
    val date get() = dateTime.date
    val time get() = dateTime.time

    override fun compareTo(other: ZonedDateTime) = instant.compareTo(instant)

    companion object {
        fun of(
            dateTime: LocalDateTime,
            timeZone: TimeZone
        ) = ZonedDateTime(
            instant = dateTime.toInstant(timeZone),
            timeZone = timeZone
        )

        fun of(
            date: LocalDate,
            time: LocalTime,
            timeZone: TimeZone
        ) = of(
            dateTime = LocalDateTime(
                date = date,
                time = time
            ),
            timeZone = timeZone
        )

        fun of(
            date: LocalDate,
            timeZone: TimeZone
        ) = of(
            date = date,
            time = LocalTime(
                hour = 0,
                minute = 0,
                second = 0,
                nanosecond = 0
            ),
            timeZone = timeZone
        )
    }
}