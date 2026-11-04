package epicarchitect.breakbadhabits.datetime

import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class MonthOfYearTest {

    @Test
    fun nextAndPreviousCrossYearBoundary() {
        assertEquals(
            MonthOfYear(2026, Month.JANUARY),
            MonthOfYear(2025, Month.DECEMBER).next()
        )
        assertEquals(
            MonthOfYear(2024, Month.DECEMBER),
            MonthOfYear(2026, Month.JANUARY).addMonths(-13)
        )
    }

    @Test
    fun countsMonthsStrictlyBetweenRange() {
        assertEquals(
            2,
            (MonthOfYear(2025, Month.JANUARY)..MonthOfYear(2025, Month.APRIL))
                .countMountsBetween()
        )
        assertEquals(
            1,
            (MonthOfYear(2025, Month.DECEMBER)..MonthOfYear(2026, Month.FEBRUARY))
                .countMountsBetween()
        )
        assertEquals(
            0,
            (MonthOfYear(2025, Month.JANUARY)..MonthOfYear(2025, Month.JANUARY))
                .countMountsBetween()
        )
    }

    @Test
    fun monthsBetweenRespectsRangeDirection() {
        val ascending = MonthOfYear(2025, Month.JANUARY)..MonthOfYear(2025, Month.APRIL)
        val descending = MonthOfYear(2025, Month.APRIL)..MonthOfYear(2025, Month.JANUARY)

        assertEquals(
            listOf(
                MonthOfYear(2025, Month.FEBRUARY),
                MonthOfYear(2025, Month.MARCH)
            ),
            ascending.mountsBetween()
        )
        assertEquals(
            listOf(
                MonthOfYear(2025, Month.MARCH),
                MonthOfYear(2025, Month.FEBRUARY)
            ),
            descending.mountsBetween()
        )
    }

    @Test
    fun instantUsesProvidedTimeZone() {
        assertEquals(
            MonthOfYear(1970, Month.JANUARY),
            Instant.fromEpochSeconds(0).monthOfYear(TimeZone.UTC)
        )
    }
}
