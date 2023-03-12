package breakbadhabits.android.app.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import kotlin.time.Duration

class DateTimeFormatter(
    private val secondText: String,
    private val minuteText: String,
    private val hourText: String,
    private val dayText: String,
) {
    private val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

    fun formatAsToDates(range: ClosedRange<LocalDate>): String {
        val start = range.start.toJavaLocalDate()
        val end = range.endInclusive.toJavaLocalDate()
        return "${formatter.format(start)} - ${formatter.format(end)}"
    }

    fun formatDuration(
        duration: Duration,
        maxValueCount: Int = 4
    ) = buildString {
        duration.inWholeDays
        val durationInSeconds = duration.inWholeSeconds
        val seconds = durationInSeconds % 60
        val minutes = durationInSeconds / 60 % 60
        val hours = durationInSeconds / 60 / 60 % 24
        val days = durationInSeconds / 60 / 60 / 24

        val appendDays = days != 0L
        val appendHours = hours != 0L && (!appendDays || maxValueCount > 1)
        val appendMinutes = minutes != 0L && (!appendDays && !appendHours || maxValueCount > 2)
        val appendSeconds = !appendDays && !appendHours && !appendMinutes || maxValueCount > 3

        if (appendDays) {
            append(days)
            append(dayText)
        }

        if (appendHours) {
            if (appendDays) append(" ")
            append(hours)
            append(hourText)
        }

        if (appendMinutes) {
            if (appendHours || appendDays) append(" ")
            append(minutes)
            append(minuteText)
        }

        if (appendSeconds) {
            if (appendMinutes || appendHours || appendDays) append(" ")
            append(seconds)
            append(secondText)
        }
    }
}