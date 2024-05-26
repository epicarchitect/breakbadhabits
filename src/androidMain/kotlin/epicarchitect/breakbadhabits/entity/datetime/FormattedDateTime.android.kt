package epicarchitect.breakbadhabits.entity.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

actual object PlatformDateTimeFormatter {
    private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    private val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

    actual fun dateTime(dateTime: DateTime): String {
        return localDateTime(dateTime.local())
    }

    actual fun monthOfYear(monthOfYear: MonthOfYear): String {
        val month = monthOfYear.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        return "$month ${monthOfYear.year}"
    }

    actual fun localDateTime(dateTime: LocalDateTime): String {
        return timeFormatter.format(dateTime.time.toJavaLocalTime()) + ", " + dateFormatter.format(dateTime.date.toJavaLocalDate())
    }

    actual fun localDate(dateTime: LocalDate): String {
        return dateFormatter.format(dateTime.toJavaLocalDate())
    }
}