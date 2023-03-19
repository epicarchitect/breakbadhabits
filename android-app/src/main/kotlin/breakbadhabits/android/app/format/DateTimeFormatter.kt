package breakbadhabits.android.app.format

import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import android.icu.util.TimeZone as AndroidTimeZone
import java.time.format.DateTimeFormatter as JavaDateTimeFormatter
import java.time.format.FormatStyle as JavaFormatStyle

class DateTimeFormatter(
    private val dateTimeConfigProvider: DateTimeConfigProvider
) {
    private val systemTimeZone get() = dateTimeConfigProvider.getConfig().systemTimeZone
    private val dateFormatter = JavaDateTimeFormatter.ofLocalizedDate(dateFormatStyle)
    private val dateTimeFormatter = JavaDateTimeFormatter.ofLocalizedDateTime(
        dateFormatStyle,
        timeFormatStyle
    )

    fun formatDate(
        instant: Instant,
    ) = formatDate(instant.toLocalDateTime(systemTimeZone).date)

    private fun formatDate(
        date: LocalDate,
    ) = dateFormatter.format(date.toJavaLocalDate())!!

    fun formatDateTime(
        instant: Instant,
    ) = formatDateTime(instant.toLocalDateTime(systemTimeZone))

    private fun formatDateTime(
        dateTime: LocalDateTime
    ) = dateTimeFormatter.format(dateTime.toJavaLocalDateTime())!!




    private fun formatTimeZone(
        timeZone: TimeZone
    ) = AndroidTimeZone.getTimeZone(timeZone.id).getDisplayName(
        false,
        timeZoneFormatStyle
    )!!

    private fun getFormattedSystemTimeZone() = formatTimeZone(systemTimeZone)

    private fun getFormattedSystemTimeZoneIfNotEquals(
        timeZone: TimeZone
    ) = if (timeZone == systemTimeZone) ""
    else "(${getFormattedSystemTimeZone()})"

    companion object {
        private val dateFormatStyle = JavaFormatStyle.LONG
        private val timeFormatStyle = JavaFormatStyle.SHORT
        private const val timeZoneFormatStyle = AndroidTimeZone.LONG_GMT
    }
}