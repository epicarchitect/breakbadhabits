package breakbadhabits.android.app.ui

import breakbadhabits.foundation.datetime.toMillis
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter

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

    fun formatDistance(
        distanceInMillis: Long,
        maxValueCount: Int = 4
    ) = buildString {
        val seconds = distanceInMillis / 1000 % 60
        val minutes = distanceInMillis / 1000 / 60 % 60
        val hours = distanceInMillis / 1000 / 60 / 60 % 24
        val days = distanceInMillis / 1000 / 60 / 60 / 24

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

    fun formatDistance(
        range: ClosedRange<LocalDateTime>,
        maxValueCount: Int = 4
    ) = formatDistance(
        distanceInMillis = if (range.endInclusive > range.start) {
            range.endInclusive.toMillis() - range.start.toMillis()
        } else {
            range.start.toMillis() - range.endInclusive.toMillis()
        },
        maxValueCount = maxValueCount
    )
}