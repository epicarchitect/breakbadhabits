package epicarchitect.breakbadhabits.operation.datetime

import androidx.compose.ui.text.intl.Locale
import kotlin.time.Duration

enum class DurationFormattingAccuracy(val order: Int) {
    DAYS(1),
    HOURS(2),
    MINUTES(3),
    SECONDS(4)
}

private val resources = if (Locale.current.language == "ru") {
    object : DurationFormattingResources {
        override fun secondsText() = "с"
        override fun minutesText() = "м"
        override fun hoursText() = "ч"
        override fun daysText() = "д"
    }
} else {
    object : DurationFormattingResources {
        override fun secondsText() = "s"
        override fun minutesText() = "m"
        override fun hoursText() = "h"
        override fun daysText() = "d"
    }
}

interface DurationFormattingResources {
    fun secondsText(): String
    fun minutesText(): String
    fun hoursText(): String
    fun daysText(): String
}

fun Duration.formatted(
    accuracy: DurationFormattingAccuracy,
    resources: DurationFormattingResources = if (Locale.current.language == "ru") {
        object : DurationFormattingResources {
            override fun secondsText() = "с"
            override fun minutesText() = "м"
            override fun hoursText() = "ч"
            override fun daysText() = "д"
        }
    } else {
        object : DurationFormattingResources {
            override fun secondsText() = "s"
            override fun minutesText() = "m"
            override fun hoursText() = "h"
            override fun daysText() = "d"
        }
    } // TODO sooo lazyyy but need move
): String {
    var result = ""

    val appendDays = onlyDays != 0L
    val appendHours = onlyHours != 0L && (!appendDays || accuracy.order > 1)
    val appendMinutes = onlyMinutes != 0L && (!appendDays && !appendHours || accuracy.order > 2)
    val appendSeconds = !appendDays && !appendHours && !appendMinutes || accuracy.order > 3

    if (appendDays) {
        result += onlyDays
        result += resources.daysText()
    }

    if (appendHours) {
        if (appendDays) result += " "
        result += onlyHours
        result += resources.hoursText()
    }

    if (appendMinutes) {
        if (appendHours || appendDays) result += " "
        result += onlyMinutes
        result += resources.minutesText()
    }

    if (appendSeconds) {
        if (appendMinutes || appendHours || appendDays) result += " "
        result += onlySeconds
        result += resources.secondsText()
    }

    return result
}