package epicarchitect.breakbadhabits.entity.datetime

import android.annotation.SuppressLint
import android.text.format.DateFormat
import epicarchitect.breakbadhabits.BreakBadHabitsApp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@SuppressLint("ConstantLocale")
actual object PlatformDateTimeFormatter {
    private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
    private val timeFormatter = DateTimeFormatter.ofPattern(timePattern())

    actual fun dateTime(dateTime: DateTime): String {
        return localDateTime(dateTime.local())
    }

    actual fun monthOfYear(monthOfYear: MonthOfYear): String {
        val month = monthOfYear.month.getDisplayName(
            TextStyle.SHORT,
            Locale.getDefault()
        ).replaceFirstChar(Char::uppercase)

        return "$month ${monthOfYear.year}"
    }

    actual fun localDateTime(dateTime: LocalDateTime): String {
        return timeFormatter.format(dateTime.time.toJavaLocalTime()) + ", " + dateFormatter.format(dateTime.date.toJavaLocalDate())
    }

    actual fun localDate(dateTime: LocalDate): String {
        return dateFormatter.format(dateTime.toJavaLocalDate())
    }

    private fun timePattern() = if (DateFormat.is24HourFormat(BreakBadHabitsApp.instance)) "HH:mm" else "hh:mm a"
}