package epicarchitect.breakbadhabits.datetime.format

import epicarchitect.breakbadhabits.format.PlatformDateTimeFormatter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class IosDateTimeFormatter : PlatformDateTimeFormatter {
    override fun format(date: LocalDate) = date.toString()
    override fun format(time: LocalTime) = time.toString()
}