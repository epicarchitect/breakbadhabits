package epicarchitect.breakbadhabits.datetime

import kotlinx.datetime.LocalDateTime

fun ClosedRange<LocalDateTime>.toLocalDateRange() = start.date..endInclusive.date