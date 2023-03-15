package breakbadhabits.foundation.datetime

import kotlinx.datetime.*
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

fun ClosedRange<MonthOfYear>.countMountsBetween(): Int {
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

fun <T : Comparable<T>> ClosedRange<T>.ascended() = if (isAscended) this else endInclusive..start

val <T : Comparable<T>> ClosedRange<T>.isAscended get() = start <= endInclusive
