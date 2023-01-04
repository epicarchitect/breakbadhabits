package breakbadhabits.extension.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime
import java.time.LocalTime

data class LocalDateInterval(
    val start: LocalDate,
    val end: LocalDate
)

fun LocalDateInterval.toLocalDateTimeInterval() = LocalDateTimeRange(
    start = LocalDateTime.of(start.toJavaLocalDate(), LocalTime.MIN).toKotlinLocalDateTime(),
    end = LocalDateTime.of(end.toJavaLocalDate(), LocalTime.MIN).toKotlinLocalDateTime()
)