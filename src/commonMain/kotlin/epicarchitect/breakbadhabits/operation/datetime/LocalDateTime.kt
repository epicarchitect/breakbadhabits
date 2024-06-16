package epicarchitect.breakbadhabits.operation.datetime

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.minus

fun ClosedRange<LocalDateTime>.toLocalDateRange() = start.date..endInclusive.date

//fun LocalDateTime.withTime(
//    hour: Int = time.hour,
//    minute: Int = time.minute,
//    second: Int = time.second
//) = LocalDateTime(date, LocalTime(hour, minute, second, 0))

fun LocalDate.yesterday() = minus(DatePeriod(days = 1))
fun LocalDateTime.yesterday() = LocalDateTime(date.yesterday(), time)