package breakbadhabits.android.app.format

import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeFormatter(
    private val dateTimeConfigProvider: DateTimeConfigProvider
) {
    private val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
    private val timeZone get() = dateTimeConfigProvider.getConfig().systemTimeZone

    fun format(instant: Instant) = format(instant.toLocalDateTime(timeZone).date)

    fun format(date: LocalDate) = formatter.format(date.toJavaLocalDate())!!

}