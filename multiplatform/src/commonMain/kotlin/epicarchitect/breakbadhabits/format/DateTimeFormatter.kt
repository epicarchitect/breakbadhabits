package epicarchitect.breakbadhabits.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

interface DateTimeFormatter {
    fun format(date: LocalDate): String
    fun format(time: LocalTime): String
}

fun DateTimeFormatter.format(dateTime: LocalDateTime): String =
    format(dateTime.date) + ", " + format(dateTime.time)

fun DateTimeFormatter.format(range: ClosedRange<LocalDateTime>): String = when {
    range.start == range.endInclusive -> format(range.start)
    range.start.date == range.endInclusive.date -> {
        format(range.start.date) + ", " +
                format(range.start.time) + " — " + format(range.endInclusive.time)
    }

    else -> format(range.start) + " — " + format(range.endInclusive)
}
