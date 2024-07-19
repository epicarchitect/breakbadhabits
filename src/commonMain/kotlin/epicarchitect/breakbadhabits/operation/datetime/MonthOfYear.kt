package epicarchitect.breakbadhabits.operation.datetime

import epicarchitect.breakbadhabits.operation.math.ranges.ascended
import epicarchitect.breakbadhabits.operation.math.ranges.isAscended
import epicarchitect.calendar.compose.basis.EpicMonth
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.floor

fun EpicMonth.toMonthOfYear() = MonthOfYear(year, month)

fun MonthOfYear.previous(): MonthOfYear = addMonths(-1)

fun MonthOfYear.next(): MonthOfYear = addMonths(1)

fun MonthOfYear.addMonths(monthsToAdd: Int): MonthOfYear {
    if (monthsToAdd == 0) return this
    val monthCount = year * 12 + (month.number - 1)
    val calcMonths = monthCount + monthsToAdd
    val newYear = floor(calcMonths.div(12.0)).toInt()
    val newMonth = floor(calcMonths.mod(12).toDouble()).toInt() + 1
    return MonthOfYear(newYear, Month(newMonth))
}

fun ClosedRange<MonthOfYear>.countMountsBetween(): Int {
    val range = ascended()
    val start = range.start
    val end = range.endInclusive
    val result = when {
        start == end -> 0
        start.year == end.year -> {
            end.month.number - start.month.number - 1
        }

        else -> {
            val yearsBetween = end.year - start.year
            end.month.number - start.month.number - 1 + yearsBetween * 12
        }
    }
    return abs(result)
}

fun ClosedRange<MonthOfYear>.mountsBetween(): List<MonthOfYear> {
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

fun ClosedRange<Instant>.monthOfYearRange(timeZone: TimeZone) = start.monthOfYear(timeZone)..endInclusive.monthOfYear(timeZone)
data class MonthOfYear(
    val year: Int,
    val month: Month
) : Comparable<MonthOfYear> {
    override fun compareTo(other: MonthOfYear): Int {
        var cmp = year - other.year
        if (cmp == 0) {
            cmp = month.number - other.month.number
        }
        return cmp
    }
}