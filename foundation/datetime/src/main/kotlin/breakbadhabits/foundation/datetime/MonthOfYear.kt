package breakbadhabits.foundation.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class MonthOfYear(
    val year: Int,
    val month: Month
) {
    companion object {
        fun now(timeZone: TimeZone = TimeZone.currentSystemDefault()): MonthOfYear {
            val now = Clock.System.now()
            val date = now.toLocalDateTime(timeZone)
            return MonthOfYear(date.year, date.month)
        }
    }
}

val LocalDate.monthOfYear get() = MonthOfYear(year, month)