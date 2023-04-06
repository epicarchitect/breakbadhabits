package breakbadhabits.android.app.format

import android.content.Context
import android.text.format.DateFormat
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter as JavaDateTimeFormatter
import java.time.format.FormatStyle as JavaFormatStyle

class DateTimeFormatter(
    private val dateTimeProvider: DateTimeProvider,
    context: Context
) {
    private val dateFormatter = JavaDateTimeFormatter.ofLocalizedDate(dateFormatStyle)
    private val timeFormatter = JavaDateTimeFormatter.ofPattern(timePattern(context))

    fun formatDateTime(
        instant: Instant,
    ) = formatDateTime(
        instant.toLocalDateTime(
            timeZone = dateTimeProvider.timeZone.value
        )
    )

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

    companion object {
        private val dateFormatStyle = JavaFormatStyle.LONG
        private fun timePattern(context: Context) =
            if (DateFormat.is24HourFormat(context)) "HH:mm" else "hh:mm a"
    }
}