package epicarchitect.breakbadhabits.format

import epicarchitect.breakbadhabits.datetime.daysPart
import epicarchitect.breakbadhabits.datetime.hoursPart
import epicarchitect.breakbadhabits.datetime.minutesPart
import epicarchitect.breakbadhabits.datetime.secondsPart
import epicarchitect.breakbadhabits.resources.strings.format.duration.DurationFormattingStrings
import kotlin.time.Duration

class DurationFormatter(
    private val strings: DurationFormattingStrings
) {
    fun format(
        duration: Duration,
        accuracy: Accuracy
    ): String {
        var result = ""

        val daysPart = duration.daysPart()
        val hoursPart = duration.hoursPart()
        val minutesPart = duration.minutesPart()
        val secondsPart = duration.secondsPart()

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

    enum class Accuracy(val order: Int) {
        DAYS(1),
        HOURS(2),
        MINUTES(3),
        SECONDS(4)
    }
}