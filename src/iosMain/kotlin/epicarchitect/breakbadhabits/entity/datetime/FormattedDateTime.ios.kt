package epicarchitect.breakbadhabits.entity.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import platform.Foundation.NSDateFormatter

actual object PlatformDateTimeFormatter {
    // TODO
    actual fun dateTime(dateTime: DateTime) = localDateTime(dateTime.local())
    actual fun monthOfYear(monthOfYear: MonthOfYear): String {
        val month = NSDateFormatter().standaloneMonthSymbols[monthOfYear.month.ordinal].toString()
        return "$month ${monthOfYear.year}"
    }

    actual fun localDateTime(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }
    actual fun localDate(dateTime: LocalDate) = dateTime.toString()
    actual fun localTime(time: LocalTime) = time.toString()

    actual fun month(month: Month) = month.toString()
}