package epicarchitect.breakbadhabits.ui.format

import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.operation.datetime.onlyDays
import epicarchitect.breakbadhabits.operation.datetime.onlyHours
import epicarchitect.breakbadhabits.operation.datetime.onlyMinutes
import epicarchitect.breakbadhabits.operation.datetime.onlySeconds
import kotlin.time.Duration

enum class DurationFormattingAccuracy(val order: Int) {
    DAYS(1),
    HOURS(2),
    MINUTES(3),
    SECONDS(4)
}

fun Duration.formatted(
    accuracy: DurationFormattingAccuracy
): String {
    val strings = AppData.resources.strings.durationFormattingStrings
    var result = ""

    val appendDays = onlyDays != 0L
    val appendHours = onlyHours != 0L && (!appendDays || accuracy.order > 1)
    val appendMinutes = onlyMinutes != 0L && (!appendDays && !appendHours || accuracy.order > 2)
    val appendSeconds = !appendDays && !appendHours && !appendMinutes || accuracy.order > 3

    if (appendDays) {
        result += onlyDays
        result += strings.daysText()
    }

    if (appendHours) {
        if (appendDays) result += " "
        result += onlyHours
        result += strings.hoursText()
    }

    if (appendMinutes) {
        if (appendHours || appendDays) result += " "
        result += onlyMinutes
        result += strings.minutesText()
    }

    if (appendSeconds) {
        if (appendMinutes || appendHours || appendDays) result += " "
        result += onlySeconds
        result += strings.secondsText()
    }

    return result
}