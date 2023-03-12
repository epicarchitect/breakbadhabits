package breakbadhabits.foundation.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime

fun Long.secondsToInstant() = Instant.fromEpochSeconds(this)

fun ClosedRange<Long>.secondsToInstantRange() =
    start.secondsToInstant()..endInclusive.secondsToInstant()

fun ClosedRange<Instant>.toDuration() = endInclusive - start

fun ClosedRange<Instant>.countDays(timeZone: TimeZone) =
    start.daysUntil(endInclusive, timeZone) + 1

fun ClosedRange<Instant>.countDaysInMonth(
    year: Int,
    month: Month,
    timeZone: TimeZone
): Int {
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