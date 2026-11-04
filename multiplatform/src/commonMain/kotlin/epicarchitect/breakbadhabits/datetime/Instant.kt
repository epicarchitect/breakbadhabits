package epicarchitect.breakbadhabits.datetime

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToLong
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.Instant
import kotlin.time.toDuration

fun ClosedRange<Instant>.toLocalDateTimeRange(
    timeZone: TimeZone
) = start.toLocalDateTime(timeZone)..endInclusive.toLocalDateTime(timeZone)

fun List<ClosedRange<Instant>>.averageDuration(): Duration? {
    val durations = map {
        it.endInclusive.epochSeconds - it.start.epochSeconds
    }
    return when {
        durations.size > 1 -> durations.average().roundToLong()
        durations.size == 1 -> durations.first()
        else -> null
    }?.toDuration(DurationUnit.SECONDS)
}

fun List<ClosedRange<Instant>>.maxDuration() = maxOfOrNull { it.endInclusive - it.start }

fun List<ClosedRange<Instant>>.minDuration() = minOfOrNull { it.endInclusive - it.start }

fun ClosedRange<Instant>.duration() = endInclusive - start
