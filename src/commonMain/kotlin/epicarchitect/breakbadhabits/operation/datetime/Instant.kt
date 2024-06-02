package epicarchitect.breakbadhabits.operation.datetime

import kotlinx.datetime.Instant
import kotlin.math.roundToLong
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

// copied from IsoChronology

fun isLeapYear(year: Long): Boolean {
    return year and 3L == 0L && (year % 100 != 0L || year % 400 == 0L)
}

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

fun Duration?.orZero() = this ?: Duration.ZERO
