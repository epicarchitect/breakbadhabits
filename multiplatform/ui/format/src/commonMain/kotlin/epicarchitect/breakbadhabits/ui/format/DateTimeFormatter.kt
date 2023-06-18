package epicarchitect.breakbadhabits.ui.format

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

interface DateTimeFormatter {
    fun formatDateTime(instant: Instant): String
    fun formatDateTime(dateTime: LocalDateTime): String
}
