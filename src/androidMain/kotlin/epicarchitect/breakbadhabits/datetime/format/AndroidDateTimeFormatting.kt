package epicarchitect.breakbadhabits.datetime.format

import android.annotation.SuppressLint
import android.text.format.DateFormat
import epicarchitect.breakbadhabits.BreakBadHabitsApp
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

// похуй + поебать на смену конфигурации, мне то что
@SuppressLint("ConstantLocale")
private val locale = Locale.getDefault()
private val dateWithOutYearFormatter = DateTimeFormatter.ofPattern("d MMMM", locale)
private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", locale)
private val timePattern = if (DateFormat.is24HourFormat(BreakBadHabitsApp.instance)) "HH:mm" else "hh:mm a"
private val timeFormatter = DateTimeFormatter.ofPattern(timePattern)

actual fun Month.formatted(): String = getDisplayName(
    TextStyle.FULL_STANDALONE,
    locale
).replaceFirstChar(Char::uppercase)

actual fun LocalDate.formatted(withYear: Boolean): String = if (withYear) {
    dateFormatter.format(toJavaLocalDate())
} else {
    dateWithOutYearFormatter.format(toJavaLocalDate())
}

actual fun LocalTime.formatted(): String = timeFormatter.format(toJavaLocalTime())