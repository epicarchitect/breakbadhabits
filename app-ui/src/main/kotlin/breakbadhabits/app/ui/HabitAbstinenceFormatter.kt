package breakbadhabits.app.ui

import android.content.Context
import breakbadhabits.entity.HabitAbstinence
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

class HabitAbstinenceFormatter(private val context: Context) {

    fun format(
        abstinence: HabitAbstinence,
        maxValueCount: Int = 4
    ) = buildString {
        val timeInMillis = abstinence.interval.value.let {
            val end = it.end.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            val start = it.start.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            end - start
        }

        val seconds = timeInMillis / 1000 % 60
        val minutes = timeInMillis / 1000 / 60 % 60
        val hours = timeInMillis / 1000 / 60 / 60 % 24
        val days = timeInMillis / 1000 / 60 / 60 / 24

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