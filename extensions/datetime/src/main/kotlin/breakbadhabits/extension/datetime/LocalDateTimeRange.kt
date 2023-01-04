package breakbadhabits.extension.datetime

import kotlinx.datetime.LocalDateTime

data class LocalDateTimeRange(
    override val start: LocalDateTime,
    override val endInclusive: LocalDateTime
) : ClosedRange<LocalDateTime>

operator fun LocalDateTime.rangeTo(end: LocalDateTime) = LocalDateTimeRange(
    start = this,
    endInclusive = end,
)
fun ad() {
    LocalDateTimeRange(
        start = LocalDateTime.parse(""),
        endInclusive = LocalDateTime.parse(""),
    )
}