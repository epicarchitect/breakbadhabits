package epicarchitect.breakbadhabits.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class DateTimeFormatter(
    private val platformDateTimeFormatter: PlatformDateTimeFormatter
) {
    fun format(date: LocalDate) = platformDateTimeFormatter.format(date)

    fun format(time: LocalTime) = platformDateTimeFormatter.format(time)

    fun format(dateTime: LocalDateTime) = format(dateTime.date) + ", " + format(dateTime.time)

    fun format(range: ClosedRange<LocalDateTime>) = when {
        range.start == range.endInclusive -> format(range.start)
        range.start.date == range.endInclusive.date -> {
            format(range.start.date) + ", " + format(range.start.time) + " — " + format(range.endInclusive.time)
        }

        else -> format(range.start) + " — " + format(range.endInclusive)
    }
}