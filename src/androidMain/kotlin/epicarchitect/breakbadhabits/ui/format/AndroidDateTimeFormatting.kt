package epicarchitect.breakbadhabits.ui.format

import android.content.Context
import android.text.format.DateFormat
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

actual fun Month.formatted(): String = getDisplayName(
    TextStyle.FULL_STANDALONE,
    niceLocale
).replaceFirstChar(Char::uppercase)

actual fun LocalDate.formatted(withYear: Boolean): String = if (withYear) {
    dateFormatter.format(toJavaLocalDate())
} else {
    dateWithOutYearFormatter.format(toJavaLocalDate())
}

actual fun LocalTime.formatted(): String = timeFormatter.format(toJavaLocalTime())

private lateinit var niceLocale: Locale
private lateinit var dateFormatter: DateTimeFormatter
private lateinit var dateWithOutYearFormatter: DateTimeFormatter
private lateinit var timePattern: String
private lateinit var timeFormatter: DateTimeFormatter

fun updateFormatters(context: Context, locale: Locale) {
    niceLocale = locale
    dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", locale)
    dateWithOutYearFormatter = DateTimeFormatter.ofPattern("d MMMM", locale)
    timePattern = if (DateFormat.is24HourFormat(context)) "HH:mm" else "hh:mm a"
    timeFormatter = DateTimeFormatter.ofPattern(timePattern)
}