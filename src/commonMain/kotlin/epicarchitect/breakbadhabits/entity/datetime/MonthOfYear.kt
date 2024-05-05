package epicarchitect.breakbadhabits.entity.datetime

import epicarchitect.breakbadhabits.entity.math.ranges.ascended
import epicarchitect.breakbadhabits.entity.math.ranges.isAscended
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.math.floor

data class MonthOfYear(
    val year: Int,
    val month: Month
) : Comparable<MonthOfYear> {
    val numberOfDays = length(isLeapYear(year.toLong()))

    private fun length(leapYear: Boolean): Int {
        return when (month) {
            Month.FEBRUARY -> if (leapYear) 29 else 28
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
            else -> 31
        }
    }

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
            cmp = month.number - other.month.number
        }
        return cmp
    }
}

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
