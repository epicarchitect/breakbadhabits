package epicarchitect.breakbadhabits.format

import epicarchitect.breakbadhabits.infrastructure.AppTimeZone
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterShortStyle

actual fun LocalDate.formatted(): String {
    val seconds = atStartOfDayIn(AppTimeZone)
        .toEpochMilliseconds() / 1000.0
    val formatter = NSDateFormatter().apply {
        dateFormat = "d MMMM yyyy"
    }
    return formatter.stringFromDate(
        NSDate(timeIntervalSinceReferenceDate = seconds - 978307200.0)
    )
}

actual fun LocalTime.formatted(): String {
    val dateTime = LocalDateTime(LocalDate(2000, 1, 1), this)
    val seconds = dateTime
        .toInstant(AppTimeZone)
        .toEpochMilliseconds() / 1000.0
    val formatter = NSDateFormatter().apply {
        timeStyle = NSDateFormatterShortStyle
    }
    return formatter.stringFromDate(
        NSDate(timeIntervalSinceReferenceDate = seconds - 978307200.0)
    )
}
