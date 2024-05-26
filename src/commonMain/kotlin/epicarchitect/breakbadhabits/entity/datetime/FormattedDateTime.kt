package epicarchitect.breakbadhabits.entity.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

expect object PlatformDateTimeFormatter {
    fun localDate(dateTime: LocalDate): String
    fun localDateTime(dateTime: LocalDateTime): String
    fun dateTime(dateTime: DateTime): String
    fun monthOfYear(monthOfYear: MonthOfYear): String
}