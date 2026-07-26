package epicarchitect.breakbadhabits.format

import epicarchitect.breakbadhabits.datetime.MonthOfYear
import kotlinx.datetime.Month
import kotlinx.datetime.format.MonthNames

class MonthFormatter(
    private val monthNames: MonthNames
) {
    fun format(month: Month) = monthNames.names[month.ordinal]

    fun format(month: MonthOfYear) = "${format(month.month)} ${month.year}"
}