package breakbadhabits.extension.datetime

import kotlinx.datetime.LocalDateTime

data class LocalDateTimeInterval(
    val start: LocalDateTime,
    val end: LocalDateTime
)