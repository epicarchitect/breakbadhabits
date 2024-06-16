package epicarchitect.breakbadhabits.ui.format

import android.annotation.SuppressLint
import android.text.format.DateFormat
import epicarchitect.breakbadhabits.BreakBadHabitsApp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Duration

actual fun Month.formatted(): String = getDisplayName(
    TextStyle.FULL_STANDALONE,
    Locale.getDefault()
).replaceFirstChar(Char::uppercase)

actual fun LocalDate.formatted(): String = dateFormatter.format(toJavaLocalDate())
actual fun LocalTime.formatted(): String = timeFormatter.format(toJavaLocalTime())

@SuppressLint("ConstantLocale")
private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
private val timePattern = if (DateFormat.is24HourFormat(BreakBadHabitsApp.instance)) "HH:mm" else "hh:mm a"
private val timeFormatter = DateTimeFormatter.ofPattern(timePattern)

fun Duration.f() {
    Instant.fromEpochSeconds(this.inWholeSeconds).toLocalDateTime(TimeZone.UTC).formatted()
}
