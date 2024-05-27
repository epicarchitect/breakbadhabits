package epicarchitect.breakbadhabits.entity.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month

expect object PlatformDateTimeFormatter {
    fun localDate(dateTime: LocalDate): String
    fun localDateTime(dateTime: LocalDateTime): String
    fun localTime(time: LocalTime): String
    fun month(month: Month): String
    fun dateTime(dateTime: DateTime): String
    fun monthOfYear(monthOfYear: MonthOfYear): String
}