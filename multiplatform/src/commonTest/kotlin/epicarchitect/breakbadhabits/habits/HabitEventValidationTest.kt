package epicarchitect.breakbadhabits.habits

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Instant

class HabitEventValidationTest {

    private val currentTime = Instant.fromEpochSeconds(1_000)

    @Test
    fun eventCountMustBePositive() {
        assertEquals(HabitEventCountError.Empty, checkHabitEventCount(0))
        assertEquals(HabitEventCountError.Empty, checkHabitEventCount(-1))
        assertNull(checkHabitEventCount(1))
    }

    @Test
    fun timeRangeCannotEndInFuture() {
        assertEquals(
            HabitEventRecordTimeRangeError.BiggestThenCurrentTime,
            checkHabitEventRecordTimeRange(
                timeRange = Instant.fromEpochSeconds(900)..Instant.fromEpochSeconds(1_001),
                currentTime = currentTime
            )
        )
    }

    @Test
    fun timeRangeCanEndNow() {
        assertNull(
            checkHabitEventRecordTimeRange(
                timeRange = Instant.fromEpochSeconds(900)..currentTime,
                currentTime = currentTime
            )
        )
    }

    @Test
    fun timeRangeCannotStartInFuture() {
        assertEquals(
            HabitEventRecordTimeRangeError.BiggestThenCurrentTime,
            checkHabitEventRecordTimeRange(
                timeRange = Instant.fromEpochSeconds(1_001)..Instant.fromEpochSeconds(1_001),
                currentTime = currentTime
            )
        )
    }
}
