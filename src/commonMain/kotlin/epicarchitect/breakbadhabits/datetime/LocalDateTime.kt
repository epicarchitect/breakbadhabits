package epicarchitect.breakbadhabits.datetime

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus
import kotlin.time.Duration.Companion.days

fun ClosedRange<LocalDateTime>.toLocalDateRange() = start.date..endInclusive.date

fun LocalDate.yesterday() = minus(DatePeriod(days = 1))

fun LocalDateTime.yesterday() = LocalDateTime(date.yesterday(), time)

fun Instant.yesterday() = minus(1.days)