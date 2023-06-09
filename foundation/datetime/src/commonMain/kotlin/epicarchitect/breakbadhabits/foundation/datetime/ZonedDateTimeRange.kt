package epicarchitect.breakbadhabits.foundation.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone


data class ZonedDateTimeRange internal constructor(
    override val start: ZonedDateTime,
    override val endInclusive: ZonedDateTime
) : ClosedRange<ZonedDateTime> {

    val timeZone get() = start.timeZone

    operator fun contains(other: ZonedDateTimeRange) = start in other && endInclusive in other

    companion object {
        fun of(value: ZonedDateTime) = ZonedDateTimeRange(value, value)

        fun of(
            start: Instant,
            endInclusive: Instant,
            timeZone: TimeZone
        ) = ZonedDateTimeRange(
            start = ZonedDateTime(
                instant = start,
                timeZone = timeZone
            ),
            endInclusive = ZonedDateTime(
                instant = endInclusive,
                timeZone = timeZone
            )
        )
    }
}

operator fun ZonedDateTime.rangeTo(end: ZonedDateTime) = ZonedDateTimeRange(this, end)

