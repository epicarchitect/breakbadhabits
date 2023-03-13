package breakbadhabits.foundation.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

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

val LocalDate.monthOfYear get() = MonthOfYear(year, month)

fun Instant.monthOfYear(timeZone: TimeZone) = toLocalDateTime(timeZone).date.monthOfYear