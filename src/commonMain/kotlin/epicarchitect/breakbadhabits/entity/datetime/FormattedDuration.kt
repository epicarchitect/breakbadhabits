package epicarchitect.breakbadhabits.entity.datetime

import androidx.compose.ui.text.intl.Locale
import kotlin.time.Duration

// TODO: resolve resources

private val resources = if (Locale.current.language == "ru") {
    object : Resources {
        override fun secondsText() = "с"
        override fun minutesText() = "м"
        override fun hoursText() = "ч"
        override fun daysText() = "д"
    }
} else {
    object : Resources {
        override fun secondsText() = "s"
        override fun minutesText() = "m"
        override fun hoursText() = "h"
        override fun daysText() = "d"
    }
}

private interface Resources {
    fun secondsText(): String
    fun minutesText(): String
    fun hoursText(): String
    fun daysText(): String
}

class FormattedDuration(
    val value: Duration,
    val accuracy: Accuracy
) {

    private val formatted by lazy {
        format(value, accuracy)
    }

    override fun toString() = formatted

    private fun format(
        duration: Duration,
        accuracy: Accuracy
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
            result += resources.daysText()
        }

        if (appendHours) {
            if (appendDays) result += " "
            result += hours
            result += resources.hoursText()
        }

        if (appendMinutes) {
            if (appendHours || appendDays) result += " "
            result += minutes
            result += resources.minutesText()
        }

        if (appendSeconds) {
            if (appendMinutes || appendHours || appendDays) result += " "
            result += seconds
            result += resources.secondsText()
        }

        return result
    }

    enum class Accuracy(val order: Int) {
        DAYS(1),
        HOURS(2),
        MINUTES(3),
        SECONDS(4)
    }
}