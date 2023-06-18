package epicarchitect.breakbadhabits.ui.format.ios

import epicarchitect.breakbadhabits.ui.format.DateTimeFormatter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

class IosDateTimeFormatter : DateTimeFormatter {
    override fun formatDateTime(instant: Instant) = instant.toString()

    override fun formatDateTime(dateTime: LocalDateTime) = dateTime.toString()
}
