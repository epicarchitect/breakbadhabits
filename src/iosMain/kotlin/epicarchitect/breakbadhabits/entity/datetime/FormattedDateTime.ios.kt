package epicarchitect.breakbadhabits.entity.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

actual object PlatformDateTimeFormatter {
    // TODO
    actual fun dateTime(dateTime: DateTime) = dateTime.local().toString()
    actual fun monthOfYear(monthOfYear: MonthOfYear) = monthOfYear.toString()
    actual fun localDateTime(dateTime: LocalDateTime) = dateTime.toString()
    actual fun localDate(dateTime: LocalDate) = dateTime.toString()
}