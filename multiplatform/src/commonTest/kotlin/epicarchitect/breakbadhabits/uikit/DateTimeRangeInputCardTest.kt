package epicarchitect.breakbadhabits.uikit

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals

class DateTimeRangeInputCardTest {

    @Test
    fun datePickerUtcMillisAreConvertedAsCalendarDate() {
        val date = LocalDate(2026, 1, 15)
        val utcMidnightMillis = LocalDateTime(
            date = date,
            time = LocalTime(hour = 0, minute = 0)
        ).toInstant(UtcOffset.ZERO).toEpochMilliseconds()

        assertEquals(date, utcDateFromEpochMilliseconds(utcMidnightMillis))
    }
}
