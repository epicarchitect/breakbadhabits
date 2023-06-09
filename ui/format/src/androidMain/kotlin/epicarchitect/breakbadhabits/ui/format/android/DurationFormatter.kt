package epicarchitect.breakbadhabits.ui.format.android

import android.content.res.Resources
import epicarchitect.breakbadhabits.foundation.datetime.onlyDays
import epicarchitect.breakbadhabits.foundation.datetime.onlyHours
import epicarchitect.breakbadhabits.foundation.datetime.onlyMinutes
import epicarchitect.breakbadhabits.foundation.datetime.onlySeconds
import epicarchitect.breakbadhabits.ui.format.DurationFormatter
import epicarchitect.breakbadhabits.ui.format.R
import kotlin.time.Duration

class AndroidDurationFormatter(
    private val resources: Resources,
    override val defaultAccuracy: DurationFormatter.Accuracy
) : DurationFormatter {
    override fun format(
        duration: Duration,
        accuracy: DurationFormatter.Accuracy
    ): String {
        var result = ""
        val seconds = duration.onlySeconds
        val minutes = duration.onlyMinutes
        val hours = duration.onlyHours
        val days = duration.onlyDays

        val appendDays = days != 0L
        val appendHours = hours != 0L && (!appendDays || accuracy.order > 1)
        val appendMinutes = minutes != 0L && (!appendDays && !appendHours || accuracy.order > 2)
        val appendSeconds = !appendDays && !appendHours && !appendMinutes || accuracy.order > 3

        if (appendDays) {
            result += days
            result += resources.getString(R.string.d)
        }

        if (appendHours) {
            if (appendDays) result += " "
            result += hours
            result += resources.getString(R.string.h)
        }

        if (appendMinutes) {
            if (appendHours || appendDays) result += " "
            result += minutes
            result += resources.getString(R.string.m)
        }

        if (appendSeconds) {
            if (appendMinutes || appendHours || appendDays) result += " "
            result += seconds
            result += resources.getString(R.string.s)
        }

        return result
    }
}