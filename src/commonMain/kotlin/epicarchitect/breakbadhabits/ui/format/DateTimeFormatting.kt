package epicarchitect.breakbadhabits.ui.format

import epicarchitect.breakbadhabits.data.datetime.MonthOfYear
import epicarchitect.breakbadhabits.operation.datetime.toLocalDateTimeRange
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

expect fun Month.formatted(): String
expect fun LocalDate.formatted(): String
expect fun LocalTime.formatted(): String

fun MonthOfYear.formatted() = "${month.formatted()} $year"

fun LocalDateTime.formatted() = time.formatted() + ", " + date.formatted()

fun Instant.formatted(timeZone: TimeZone) = toLocalDateTime(timeZone).formatted()

fun ClosedRange<LocalDateTime>.formatted() = when {
    start == endInclusive           -> start.formatted()
    start.date == endInclusive.date -> {
        start.time.formatted() + " — " + endInclusive.time.formatted() + ", " + start.date.formatted()
    }
    else                            -> start.formatted() + " — " + endInclusive.formatted()
}

fun ClosedRange<Instant>.formatted(timeZone: TimeZone) = toLocalDateTimeRange(timeZone).formatted()
