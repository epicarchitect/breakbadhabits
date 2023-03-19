package breakbadhabits.foundation.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToLong
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Long.secondsToInstant() = Instant.fromEpochSeconds(this)

fun ClosedRange<java.time.LocalDateTime>.toKotlinRange() =
    start.toKotlinLocalDateTime()..endInclusive.toKotlinLocalDateTime()

fun ClosedRange<Instant>.toJavaLocalDateTimeRange(
    timeZone: TimeZone
) = start.toLocalDateTime(timeZone)
    .toJavaLocalDateTime()..endInclusive
    .toLocalDateTime(timeZone)
    .toJavaLocalDateTime()

fun ClosedRange<LocalDateTime>.toInstantRange(timeZone: TimeZone) =
    start.toInstant(timeZone)..endInclusive.toInstant(timeZone)

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

fun List<ClosedRange<Instant>>.averageDuration() = map {
    it.endInclusive.epochSeconds - it.start.epochSeconds
}.let {
    if (it.size > 1) it.average().roundToLong()
    else it.first()
}.toDuration(DurationUnit.SECONDS)

fun List<ClosedRange<Instant>>.maxDuration() = maxOf { it.toDuration() }

fun List<ClosedRange<Instant>>.minDuration() = minOf { it.toDuration() }