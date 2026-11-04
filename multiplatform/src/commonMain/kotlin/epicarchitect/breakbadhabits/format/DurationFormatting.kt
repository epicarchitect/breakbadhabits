package epicarchitect.breakbadhabits.format

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.datetime.daysPart
import epicarchitect.breakbadhabits.datetime.hoursPart
import epicarchitect.breakbadhabits.datetime.minutesPart
import epicarchitect.breakbadhabits.datetime.secondsPart
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.duration_days
import epicarchitect.breakbadhabits.resources.duration_hours
import epicarchitect.breakbadhabits.resources.duration_minutes
import epicarchitect.breakbadhabits.resources.duration_seconds
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Duration

enum class Accuracy(val order: Int) {
    DAYS(1),
    HOURS(2),
    MINUTES(3),
    SECONDS(4)
}

@Composable
fun Duration.formatted(accuracy: Accuracy = Accuracy.SECONDS): String = formatted(
    accuracy = accuracy,
    daysText = stringResource(Res.string.duration_days),
    hoursText = stringResource(Res.string.duration_hours),
    minutesText = stringResource(Res.string.duration_minutes),
    secondsText = stringResource(Res.string.duration_seconds)
)

suspend fun Duration.formattedText(accuracy: Accuracy): String = formatted(
    accuracy = accuracy,
    daysText = getString(Res.string.duration_days),
    hoursText = getString(Res.string.duration_hours),
    minutesText = getString(Res.string.duration_minutes),
    secondsText = getString(Res.string.duration_seconds)
)

fun Duration.formatted(
    accuracy: Accuracy,
    daysText: String,
    hoursText: String,
    minutesText: String,
    secondsText: String
): String {
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
        result += daysText
    }

    if (appendHours) {
        if (appendDays) result += " "
        result += hoursPart
        result += hoursText
    }

    if (appendMinutes) {
        if (appendHours || appendDays) result += " "
        result += minutesPart
        result += minutesText
    }

    if (appendSeconds) {
        if (appendMinutes || appendHours || appendDays) result += " "
        result += secondsPart
        result += secondsText
    }

    return result
}
