package epicarchitect.breakbadhabits.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

expect fun LocalDate.formatted(): String

expect fun LocalTime.formatted(): String

fun LocalDateTime.formatted(): String =
    date.formatted() + ", " + time.formatted()

fun ClosedRange<LocalDateTime>.formatted(): String = when {
    start == endInclusive -> start.formatted()
    start.date == endInclusive.date -> {
        start.date.formatted() + ", " +
                start.time.formatted() + " — " + endInclusive.time.formatted()
    }

    else -> start.formatted() + " — " + endInclusive.formatted()
}
