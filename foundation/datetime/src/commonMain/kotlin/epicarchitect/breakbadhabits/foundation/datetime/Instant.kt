package epicarchitect.breakbadhabits.foundation.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToLong
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val ZonedDateTimeRange.duration get() = endInclusive.instant - start.instant

fun ZonedDateTimeRange.split(step: Duration): List<ZonedDateTimeRange> {
    var current = start
    return buildList {
        while (current < endInclusive) {
            val newEnd = (current.instant + step).let {
                if (it < endInclusive.instant) it
                else endInclusive.instant
            }
            add(
                ZonedDateTimeRange(
                    current,
                    current.copy(
                        instant = newEnd
                    ).also { current = it }
                )
            )
        }
    }
}

fun ZonedDateTimeRange.countDays() =
    start.instant.daysUntil(endInclusive.instant, timeZone) + 1

//fun InstantRange.countDays(timeZone: TimeZone) =
//    start.daysUntil(endInclusive, timeZone) + 1
//
//fun InstantRange.countDaysInMonth(
//    monthOfYear: MonthOfYear,
//    timeZone: TimeZone
//): Int {
//    val month = monthOfYear.month
//    val year = monthOfYear.year
//    val startDate = start.toLocalDateTime(timeZone).date
//    val endDate = endInclusive.toLocalDateTime(timeZone).date
//    val lengthOfMonth = month.length(isLeapYear(year.toLong()))
//
//    val startDateInMonth = startDate.year == year && startDate.month == month
//    val startDateBeforeMonth = startDate.year < year || startDate.month < month
//    val endDateInMonth = endDate.year == year && endDate.month == month
//    val endDateAfterMonth = endDate.year > year || endDate.month > month
//
//    return when {
//        startDateInMonth && endDateInMonth -> {
//            countDays(timeZone)
//        }
//
//        startDateInMonth && endDateAfterMonth -> {
//            lengthOfMonth - (startDate.dayOfMonth - 1)
//        }
//
//        startDateBeforeMonth && endDateInMonth -> {
//            lengthOfMonth - (lengthOfMonth - endDate.dayOfMonth)
//        }
//
//        startDateBeforeMonth && endDateAfterMonth -> {
//            lengthOfMonth
//        }
//
//        else -> 0
//    }
//}

// copied from IsoChronology
fun isLeapYear(year: Long): Boolean {
    return year and 3L == 0L && (year % 100 != 0L || year % 400 == 0L)
}

operator fun ZonedDateTime.minus(duration: Duration) = ZonedDateTime(
    instant = instant - duration,
    timeZone = timeZone
)

operator fun ZonedDateTime.minus(other: ZonedDateTime) = instant - other.instant

fun List<ZonedDateTimeRange>.averageDuration() = map {
    it.endInclusive.instant.epochSeconds - it.start.instant.epochSeconds
}.let {
    if (it.size > 1) it.average().roundToLong()
    else it.first()
}.toDuration(DurationUnit.SECONDS)

fun List<ZonedDateTimeRange>.maxDuration() = maxOf { it.duration }

fun List<ZonedDateTimeRange>.minDuration() = minOf { it.duration }

fun List<InstantRange>.toLocalDateRanges(timeZone: TimeZone) = map {
    it.start.toLocalDateTime(timeZone).date..it.endInclusive.toLocalDateTime(timeZone).date
}

fun ZonedDateTimeRange.withZeroSeconds() =
    start.withZeroSeconds()..endInclusive.withZeroSeconds()

fun InstantRange.withZeroSeconds(timeZone: TimeZone) =
    start.withZeroSeconds(timeZone)..endInclusive.withZeroSeconds(timeZone)


fun ZonedDateTime.withZeroSeconds() = ZonedDateTime(
    instant.withZeroSeconds(timeZone),
    timeZone
)

fun Instant.withZeroSeconds(timeZone: TimeZone): Instant {
    val initial = toLocalDateTime(timeZone)
    val fixed = LocalDateTime(
        date = initial.date,
        time = LocalTime(
            hour = initial.hour,
            minute = initial.minute,
            second = 0,
            nanosecond = 0
        )
    )
    return fixed.toInstant(timeZone)
}