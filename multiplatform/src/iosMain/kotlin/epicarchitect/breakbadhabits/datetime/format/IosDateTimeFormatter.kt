package epicarchitect.breakbadhabits.datetime.format

import epicarchitect.breakbadhabits.format.DateTimeFormatter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter

class IosDateTimeFormatter : DateTimeFormatter {
    override fun format(date: LocalDate): String = format(date, "d MMMM yyyy")

    override fun format(time: LocalTime): String {
        val dateTime = LocalDateTime(LocalDate(2000, 1, 1), time)
        val seconds = dateTime
            .toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds() / 1000.0
        val formatter = NSDateFormatter().apply {
            timeStyle = platform.Foundation.NSDateFormatterShortStyle
        }
        return formatter.stringFromDate(NSDate(timeIntervalSinceReferenceDate = seconds - 978307200.0))
    }

    private fun format(date: LocalDate, pattern: String): String {
        val seconds = date
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds() / 1000.0
        val formatter = NSDateFormatter().apply {
            dateFormat = pattern
        }
        return formatter.stringFromDate(NSDate(timeIntervalSinceReferenceDate = seconds - 978307200.0))
    }
}
