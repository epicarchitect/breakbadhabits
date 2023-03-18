package breakbadhabits.android.app.format

import android.content.res.Resources
import breakbadhabits.android.app.R
import breakbadhabits.foundation.datetime.onlyDays
import breakbadhabits.foundation.datetime.onlyHours
import breakbadhabits.foundation.datetime.onlyMinutes
import breakbadhabits.foundation.datetime.onlySeconds
import kotlin.time.Duration

class DurationFormatter(
    private val resources: Resources,
    private val defaultAccuracy: Accuracy
) {
    fun format(
        duration: Duration,
        accuracy: Accuracy = defaultAccuracy
    ) = buildString {
        val seconds = duration.onlySeconds
        val minutes = duration.onlyMinutes
        val hours = duration.onlyHours
        val days = duration.onlyDays

        val appendDays = days != 0L
        val appendHours = hours != 0L && (!appendDays || accuracy.order > 1)
        val appendMinutes = minutes != 0L && (!appendDays && !appendHours || accuracy.order > 2)
        val appendSeconds = !appendDays && !appendHours && !appendMinutes || accuracy.order > 3

        if (appendDays) {
            append(days)
            append(resources.getString(R.string.d))
        }

        if (appendHours) {
            if (appendDays) append(" ")
            append(hours)
            append(resources.getString(R.string.h))
        }

        if (appendMinutes) {
            if (appendHours || appendDays) append(" ")
            append(minutes)
            append(resources.getString(R.string.m))
        }

        if (appendSeconds) {
            if (appendMinutes || appendHours || appendDays) append(" ")
            append(seconds)
            append(resources.getString(R.string.s))
        }
    }

    enum class Accuracy(val order: Int) {
        DAYS(1),
        HOURS(2),
        MINUTES(3),
        SECONDS(4)
    }
}