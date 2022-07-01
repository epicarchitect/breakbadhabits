package breakbadhabits.android.app.formatter

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

@SuppressLint("SimpleDateFormat")
class DateTimeFormatter(context: Context) {

    private val timePattern = if (DateFormat.is24HourFormat(context)) "HH:mm" else "hh:mm a"
    private val datePattern = "d MMMM yyyy"
    private val dateWithoutYearPattern = "d MMMM"

    fun formatTime(calendar: Calendar) = SimpleDateFormat(timePattern).format(calendar.time)

    fun formatDate(calendar: Calendar, withoutYear: Boolean = false) = SimpleDateFormat(
        if (withoutYear) dateWithoutYearPattern else datePattern
    ).format(calendar.time)

    fun formatDateTime(calendar: Calendar, withoutYear: Boolean = false) = SimpleDateFormat(
        "${if (withoutYear) dateWithoutYearPattern else datePattern}, $timePattern"
    ).format(calendar.time)

}