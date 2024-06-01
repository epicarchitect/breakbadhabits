package epicarchitect.breakbadhabits.operation.datetime

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

actual fun Month.formatted(): String = getDisplayName(
    TextStyle.FULL_STANDALONE,
    Locale.getDefault()
).replaceFirstChar(Char::uppercase)

actual fun LocalDate.formatted(): String = dateFormatter().format(toJavaLocalDate())
actual fun LocalTime.formatted(): String = timeFormatter().format(toJavaLocalTime())

private fun dateFormatter() = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
private fun timeFormatter() = DateTimeFormatter.ofPattern(timePattern())
private fun timePattern() = if (DateFormat.is24HourFormat(BreakBadHabitsApp.instance)) "HH:mm" else "hh:mm a"