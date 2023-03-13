package breakbadhabits.foundation.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

data class MonthOfYear(
    val year: Int,
    val month: Month
)

val LocalDate.monthOfYear get() = MonthOfYear(year, month)