package epicarchitect.breakbadhabits.foundation.datetime

import epicarchitect.breakbadhabits.foundation.math.ranges.ascended
import epicarchitect.breakbadhabits.foundation.math.ranges.isAscended
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs

data class MonthOfYear(
    val year: Int,
    val month: Month
) : Comparable<MonthOfYear> {
    companion object {
        fun now(timeZone: TimeZone): MonthOfYear {
            val now = Clock.System.now()
            val date = now.toLocalDateTime(timeZone)
            return MonthOfYear(date.year, date.month)
        }
    }

    override fun compareTo(other: MonthOfYear): Int {
        var cmp = year - other.year
        if (cmp == 0) {
            cmp = month.value - other.month.value
        }
        return cmp
    }
}

fun MonthOfYear.length() = month.length(isLeapYear(year.toLong()))

fun MonthOfYear.previous() = when {
    month.value - 1 == 0 -> {
        copy(
            month = Month.of(12),
            year = year - 1
        )
    }
    else -> {
        copy(
            month = month.minus(1),
        )
    }
}

fun MonthOfYear.next() = when {
    month.value + 1 == 13 -> {
        copy(
            month = Month.of(1),
            year = year + 1
        )
    }
    else -> {
        copy(
            month = month.plus(1),
        )
    }
}

fun MonthOfYear.addMonths(monthsToAdd: Int): MonthOfYear {
    if (monthsToAdd == 0) return this
    val monthCount = year * 12 + (month.value - 1)
    val calcMonths = monthCount + monthsToAdd
    val newYear = Math.floorDiv(calcMonths, 12)
    val newMonth = Math.floorMod(calcMonths, 12) + 1
    return MonthOfYear(newYear, Month(newMonth))
}

fun MonthOfYearRange.countMountsBetween(): Int {
    val range = ascended()
    val start = range.start
    val end = range.endInclusive
    val result = when {
        start == end -> 0
        start.year == end.year -> {
            end.month.value - start.month.value - 1
        }
        else -> {
            val yearsBetween = end.year - start.year
            end.month.value - start.month.value - 1 + yearsBetween * 12
        }
    }
    return abs(result)
}

fun MonthOfYearRange.mountsBetween(): List<MonthOfYear> {
    val mountsBetweenCount = countMountsBetween()
    if (mountsBetweenCount == 0) return emptyList()

    return buildList {
        var last = start
        repeat(mountsBetweenCount) {
            last = if (isAscended) last.next() else last.previous()
            add(last)
        }
    }
}

val LocalDate.monthOfYear get() = MonthOfYear(year, month)

fun Instant.monthOfYear(timeZone: TimeZone) = toLocalDateTime(timeZone).date.monthOfYear

fun InstantRange.monthOfYearRange(timeZone: TimeZone): MonthOfYearRange =
    start.monthOfYear(timeZone)..endInclusive.monthOfYear(timeZone)

fun ZonedDateTimeRange.monthOfYearRange(): MonthOfYearRange =
    start.dateTime.date.monthOfYear..endInclusive.dateTime.date.monthOfYear

fun MonthOfYear.atDay(dayOfMonth: Int) = LocalDate(year, month, dayOfMonth)

fun MonthOfYear.lastDayOfWeek() = atDay(length()).dayOfWeek

fun MonthOfYear.toInstantRange(timeZone: TimeZone): InstantRange {
    val start = LocalDateTime(year, month, 1, 0, 0, 0)
    val end = LocalDateTime(year, month, length(), 23, 59, 59)
    return start.toInstant(timeZone)..end.toInstant(timeZone)
}

fun MonthOfYear.toInstant(
    timeZone: TimeZone,
    dayOfMonth: Int,
    hour: Int,
    minute: Int,
    second: Int
) = LocalDateTime(year, month, dayOfMonth, hour, minute, second).toInstant(timeZone)

fun MonthOfYear.toInstantAtStart(timeZone: TimeZone) = toInstant(
    timeZone = timeZone,
    dayOfMonth = 1,
    hour = 0,
    minute = 0,
    second = 0
)

fun MonthOfYear.toInstantAtEnd(timeZone: TimeZone) = toInstant(
    timeZone = timeZone,
    dayOfMonth = length(),
    hour = 23,
    minute = 59,
    second = 59
)


fun MonthOfYear.toZonedDateTimeAtStart(timeZone: TimeZone) = ZonedDateTime(
    toInstantAtStart(timeZone),
    timeZone
)

fun MonthOfYear.toZonedDateTimeAtEnd(timeZone: TimeZone) = ZonedDateTime(
    toInstantAtStart(timeZone),
    timeZone
)