package epicarchitect.breakbadhabits.ui.format

import epicarchitect.breakbadhabits.environment.datetime.MonthOfYear
import epicarchitect.breakbadhabits.operation.datetime.toLocalDateTimeRange
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

expect fun Month.formatted(): String
expect fun LocalDate.formatted(withYear: Boolean = false): String
expect fun LocalTime.formatted(): String

fun MonthOfYear.formatted() = "${month.formatted()} $year"

fun LocalDateTime.formatted(withYear: Boolean = false) = date.formatted(withYear) + ", " + time.formatted()

fun Instant.formatted(timeZone: TimeZone, withYear: Boolean = false) = toLocalDateTime(timeZone).formatted(withYear)

fun ClosedRange<LocalDateTime>.formatted(withYear: Boolean = false) = when {
    start == endInclusive -> start.formatted(withYear)
    start.date == endInclusive.date -> {
        start.date.formatted(withYear) + ", " + start.time.formatted() + " — " + endInclusive.time.formatted()
    }

    else -> start.formatted(withYear) + " — " + endInclusive.formatted(withYear)
}

fun ClosedRange<Instant>.formatted(timeZone: TimeZone, withYear: Boolean = false) =
    toLocalDateTimeRange(timeZone).formatted(withYear)
