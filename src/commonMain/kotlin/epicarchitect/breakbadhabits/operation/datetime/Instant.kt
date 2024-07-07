package epicarchitect.breakbadhabits.operation.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToLong
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun ClosedRange<Instant>.toLocalDateTimeRange(
    timeZone: TimeZone
) = start.toLocalDateTime(timeZone)..endInclusive.toLocalDateTime(timeZone)

fun ClosedRange<LocalDateTime>.toInstantRange(
    timeZone: TimeZone
) = start.toInstant(timeZone)..endInclusive.toInstant(timeZone)


fun List<ClosedRange<Instant>>.averageDuration() = map {
    it.endInclusive.epochSeconds - it.start.epochSeconds
}.let {
    if (it.size > 1) {
        it.average().roundToLong()
    } else if (it.size == 1) {
        it.first()
    } else {
        null
    }
}?.toDuration(DurationUnit.SECONDS)

fun List<ClosedRange<Instant>>.maxDuration() = maxOfOrNull { it.endInclusive - it.start }

fun List<ClosedRange<Instant>>.minDuration() = minOfOrNull { it.endInclusive - it.start }

fun ClosedRange<Instant>.duration() = endInclusive - start
