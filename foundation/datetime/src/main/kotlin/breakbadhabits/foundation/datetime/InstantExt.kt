package breakbadhabits.foundation.datetime

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Long.secondsToInstant() = Instant.fromEpochSeconds(this)

fun ClosedRange<Long>.secondsToInstantRange() =
    start.secondsToInstant()..endInclusive.secondsToInstant()

fun ClosedRange<Instant>.toDuration() = endInclusive - start

fun ClosedRange<Instant>.countDays(timeZone: TimeZone) =
    start.daysUntil(endInclusive, timeZone) + 1

fun ClosedRange<Instant>.countDaysInMonth(
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

fun Instant.atStartOfDay(
    timeZone: TimeZone
) = toLocalDateTime(timeZone).date.atStartOfDayIn(timeZone)

fun Instant.atMiddleOfDay(
    timeZone: TimeZone
) = atStartOfDay(timeZone).plus(12, DateTimeUnit.HOUR)

fun ClosedRange<Instant>.asOneDayOrNull(timeZone: TimeZone): LocalDate? {
    val startDate = start.toLocalDateTime(timeZone).date
    val endDate = endInclusive.toLocalDateTime(timeZone).date
    return if (startDate == endDate) startDate
    else null
}

fun List<ClosedRange<Instant>>.averageDuration() = map {
    it.endInclusive.epochSeconds - it.start.epochSeconds
}.average().toDuration(DurationUnit.SECONDS)

fun List<ClosedRange<Instant>>.maxDuration() = maxOf { it.toDuration() }

fun List<ClosedRange<Instant>>.minDuration() = minOf { it.toDuration() }