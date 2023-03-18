package breakbadhabits.android.app.format

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeFormatter {
    private val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

    fun format(
        instant: Instant,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ) = format(instant.toLocalDateTime(timeZone).date)

    fun format(date: LocalDate) = formatter.format(date.toJavaLocalDate())!!

}