package epicarchitect.breakbadhabits.environment.datetime

import kotlinx.datetime.Month
import kotlinx.datetime.number

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