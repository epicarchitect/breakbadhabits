package epicarchitect.breakbadhabits.data.datetime

import epicarchitect.breakbadhabits.operation.datetime.isLeapYear
import kotlinx.datetime.Clock
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

data class MonthOfYear(
    val year: Int,
    val month: Month
) : Comparable<MonthOfYear> {
    val numberOfDays = length(isLeapYear(year.toLong()))

    private fun length(leapYear: Boolean): Int {
        return when (month) {
            Month.FEBRUARY                                           -> if (leapYear) 29 else 28
            Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
            else                                                     -> 31
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