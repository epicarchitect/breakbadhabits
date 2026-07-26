package epicarchitect.calendar.compose.basis

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.toJavaDayOfWeek
import kotlinx.datetime.toKotlinDayOfWeek
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

actual fun firstDayOfWeekBySystem(): DayOfWeek =
    WeekFields.of(Locale.getDefault()).firstDayOfWeek!!.toKotlinDayOfWeek()

actual fun DayOfWeek.localizedBySystem(): String =
    toJavaDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault())!!