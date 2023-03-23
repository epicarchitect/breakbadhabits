package breakbadhabits.android.app.format

import android.content.Context
import android.text.format.DateFormat
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
    private val dateTimeConfigProvider: DateTimeConfigProvider,
    context: Context
) {
    private val appTimeZone get() = dateTimeConfigProvider.getConfig().appTimeZone
    private val dateFormatter = JavaDateTimeFormatter.ofLocalizedDate(dateFormatStyle)
    private val timeFormatter = JavaDateTimeFormatter.ofPattern(
        if (DateFormat.is24HourFormat(context)) "HH:mm"
        else "hh:mm a"
    )

    fun formatDate(
        instant: Instant,
    ) = formatDate(instant.toLocalDateTime(appTimeZone).date)

    private fun formatDate(
        date: LocalDate,
    ) = dateFormatter.format(date.toJavaLocalDate())!!

    fun formatDateTime(
        instant: Instant,
    ) = formatDateTime(instant.toLocalDateTime(appTimeZone))

    private fun formatDateTime(
        dateTime: LocalDateTime
    ): String {
        val jDateTime = dateTime.toJavaLocalDateTime()
        return dateFormatter.format(
            jDateTime.toLocalDate()
        ) + ", " + timeFormatter.format(
            jDateTime.toLocalTime()
        )
    }

    private fun formatTimeZone(
        timeZone: TimeZone
    ) = AndroidTimeZone.getTimeZone(timeZone.id).getDisplayName(
        false,
        timeZoneFormatStyle
    )!!

    companion object {
        private val dateFormatStyle = JavaFormatStyle.LONG
        private const val timeZoneFormatStyle = AndroidTimeZone.LONG_GMT
    }
}