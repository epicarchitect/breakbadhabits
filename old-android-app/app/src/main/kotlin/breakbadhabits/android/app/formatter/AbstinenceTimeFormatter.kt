package breakbadhabits.android.app.formatter

import android.content.Context
import breakbadhabits.android.app.R

class AbstinenceTimeFormatter(private val context: Context) {
    fun format(time: Long, maxValueCount: Int = 4): String = buildString {
        val seconds = time / 1000 % 60
        val minutes = time / 1000 / 60 % 60
        val hours = time / 1000 / 60 / 60 % 24
        val days = time / 1000 / 60 / 60 / 24

        val appendDays = days != 0L
        val appendHours = hours != 0L && (!appendDays || maxValueCount > 1)
        val appendMinutes = minutes != 0L && (!appendDays && !appendHours || maxValueCount > 2)
        val appendSeconds = !appendDays && !appendHours && !appendMinutes || maxValueCount > 3

        if (appendDays) {
            append(days)
            append(context.getString(R.string.d))
        }

        if (appendHours) {
            if (appendDays) append(" ")
            append(hours)
            append(context.getString(R.string.h))
        }

        if (appendMinutes) {
            if (appendHours || appendDays) append(" ")
            append(minutes)
            append(context.getString(R.string.m))
        }

        if (appendSeconds) {
            if (appendMinutes || appendHours || appendDays) append(" ")
            append(seconds)
            append(context.getString(R.string.s))
        }
    }
}