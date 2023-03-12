package breakbadhabits.foundation.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import java.time.YearMonth

fun Long.secondsToInstant() = Instant.fromEpochSeconds(this)

fun ClosedRange<Long>.secondsToInstantRange() =
    start.secondsToInstant()..endInclusive.secondsToInstant()

fun ClosedRange<Instant>.toDuration() = if (endInclusive > start) {
    endInclusive - start
} else {
    start - endInclusive
}

fun ClosedRange<Instant>.countDays(timeZone: TimeZone) =
    start.daysUntil(endInclusive, timeZone) + 1

fun ClosedRange<Instant>.countDaysInMonth(
    yearMonth: YearMonth,
    timeZone: TimeZone
): Int {
    val startDate = start.toLocalDateTime(timeZone).date
    val endDate = endInclusive.toLocalDateTime(timeZone).date
    val lengthOfMonth = yearMonth.lengthOfMonth()

    return when {
        startDate.month == yearMonth.month && endDate.month == yearMonth.month -> {
            countDays(timeZone)
        }
        startDate.month == yearMonth.month && endDate.month > yearMonth.month -> {
            lengthOfMonth - (startDate.dayOfMonth - 1)
        }
        startDate.month < yearMonth.month && endDate.month == yearMonth.month -> {
            lengthOfMonth - (lengthOfMonth - endDate.dayOfMonth)
        }
        startDate.month < yearMonth.month && endDate.month > yearMonth.month -> {
            lengthOfMonth
        }
        else -> 0
    }
}