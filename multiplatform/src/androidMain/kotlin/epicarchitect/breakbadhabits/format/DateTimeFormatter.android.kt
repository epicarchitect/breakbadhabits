package epicarchitect.breakbadhabits.format

import android.text.format.DateFormat
import epicarchitect.breakbadhabits.infrastructure.Application
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

actual fun LocalDate.formatted(): String =
    DateTimeFormatter.ofPattern("d MMMM yyyy", currentLocale())
        .format(toJavaLocalDate())

actual fun LocalTime.formatted(): String {
    val pattern = if (is24HourFormat()) "HH:mm" else "hh:mm a"
    return DateTimeFormatter.ofPattern(pattern, currentLocale())
        .format(toJavaLocalTime())
}

private fun currentLocale(): Locale =
    Application.resources.configuration.locales[0]

private fun is24HourFormat(): Boolean =
    DateFormat.is24HourFormat(Application)
