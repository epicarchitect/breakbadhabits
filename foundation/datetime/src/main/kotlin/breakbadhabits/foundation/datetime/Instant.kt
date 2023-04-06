package breakbadhabits.foundation.datetime

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToLong
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val InstantRange.duration get() = endInclusive - start

fun InstantRange.toLocalDateList(timeZone: TimeZone): List<LocalDate> {
    var current = start.toLocalDateTime(timeZone).date
    return List(countDays(timeZone)) {
        current.also {
            current = current.plus(DateTimeUnit.DAY)
        }
    }
}

fun InstantRange.toLocalDateTimeRange(
    timeZone: TimeZone
): LocalDateTimeRange = start.toLocalDateTime(timeZone)..endInclusive.toLocalDateTime(timeZone)

fun InstantRange.toLocalDateRange(
    timeZone: TimeZone
): LocalDateRange = toLocalDateTimeRange(timeZone).let { it.start.date..it.endInclusive.date }

fun InstantRange.countDays(timeZone: TimeZone) =
    start.daysUntil(endInclusive, timeZone) + 1

fun InstantRange.countDaysInMonth(
    monthOfYear: MonthOfYear,
    timeZone: TimeZone
): Int {
    val month = monthOfYear.month
    val year = monthOfYear.year
    val startDate = start.toLocalDateTime(timeZone).date
    val endDate = endInclusive.toLocalDateTime(timeZone).date
    val lengthOfMonth = month.length(isLeapYear(year.toLong()))

    val startDateInMonth = startDate.year == year && startDate.month == month
    val startDateBeforeMonth = startDate.year < year || startDate.month < month
    val endDateInMonth = endDate.year == year && endDate.month == month
    val endDateAfterMonth = endDate.year > year || endDate.month > month

    return when {
        startDateInMonth && endDateInMonth -> {
            countDays(timeZone)
        }
        startDateInMonth && endDateAfterMonth -> {
            lengthOfMonth - (startDate.dayOfMonth - 1)
        }
        startDateBeforeMonth && endDateInMonth -> {
            lengthOfMonth - (lengthOfMonth - endDate.dayOfMonth)
        }
        startDateBeforeMonth && endDateAfterMonth -> {
            lengthOfMonth
        }
        else -> 0
    }
}

// copied from IsoChronology
fun isLeapYear(year: Long): Boolean {
    return year and 3L == 0L && (year % 100 != 0L || year % 400 == 0L)
}

fun List<InstantRange>.averageDuration() = map {
    it.endInclusive.epochSeconds - it.start.epochSeconds
}.let {
    if (it.size > 1) it.average().roundToLong()
    else it.first()
}.toDuration(DurationUnit.SECONDS)

fun List<InstantRange>.maxDuration() = maxOf { it.duration }

fun List<InstantRange>.minDuration() = minOf { it.duration }

fun List<InstantRange>.toLocalDateRanges(timeZone: TimeZone) = map {
    it.start.toLocalDateTime(timeZone).date..it.endInclusive.toLocalDateTime(timeZone).date
}

fun InstantRange.withZeroSeconds(timeZone: TimeZone) =
    start.withZeroSeconds(timeZone)..endInclusive.withZeroSeconds(timeZone)

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