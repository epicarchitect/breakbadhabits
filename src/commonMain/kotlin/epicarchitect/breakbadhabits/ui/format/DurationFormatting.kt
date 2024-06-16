package epicarchitect.breakbadhabits.ui.format

import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.operation.datetime.daysPart
import epicarchitect.breakbadhabits.operation.datetime.hoursPart
import epicarchitect.breakbadhabits.operation.datetime.minutesPart
import epicarchitect.breakbadhabits.operation.datetime.secondsPart
import kotlin.time.Duration

enum class DurationFormattingAccuracy(val order: Int) {
    DAYS(1),
    HOURS(2),
    MINUTES(3),
    SECONDS(4)
}

fun Duration.formatted(
    accuracy: DurationFormattingAccuracy = DurationFormattingAccuracy.DAYS
): String {
    val strings = AppData.resources.strings.durationFormattingStrings
    var result = ""

    val daysPart = daysPart()
    val hoursPart = hoursPart()
    val minutesPart = minutesPart()
    val secondsPart = secondsPart()

    val appendDays = daysPart != 0L
    val appendHours = hoursPart != 0L && (!appendDays || accuracy.order > 1)
    val appendMinutes = minutesPart != 0L && (!appendDays && !appendHours || accuracy.order > 2)
    val appendSeconds = !appendDays && !appendHours && !appendMinutes || accuracy.order > 3

    if (appendDays) {
        result += daysPart
        result += strings.daysText()
    }

    if (appendHours) {
        if (appendDays) result += " "
        result += hoursPart
        result += strings.hoursText()
    }

    if (appendMinutes) {
        if (appendHours || appendDays) result += " "
        result += minutesPart
        result += strings.minutesText()
    }

    if (appendSeconds) {
        if (appendMinutes || appendHours || appendDays) result += " "
        result += secondsPart
        result += strings.secondsText()
    }

    return result
}