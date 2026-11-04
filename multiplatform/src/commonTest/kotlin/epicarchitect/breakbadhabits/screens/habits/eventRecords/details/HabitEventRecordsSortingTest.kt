package epicarchitect.breakbadhabits.screens.habits.eventRecords.details

import epicarchitect.breakbadhabits.database.HabitEventRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant
import kotlin.uuid.Uuid

class HabitEventRecordsSortingTest {

    @Test
    fun recordsAreSortedNewestFirst() {
        val habitId = Uuid.random()
        val now = Instant.fromEpochSeconds(1_800_000_000)
        val old = HabitEventRecord(
            id = Uuid.random(),
            habitId = habitId,
            startTime = now - 2.days,
            endTime = now - 2.days,
            eventCount = 1,
            comment = "old"
        )
        val latest = HabitEventRecord(
            id = Uuid.random(),
            habitId = habitId,
            startTime = now,
            endTime = now,
            eventCount = 1,
            comment = "latest"
        )
        val middle = HabitEventRecord(
            id = Uuid.random(),
            habitId = habitId,
            startTime = now - 1.days,
            endTime = now - 1.days,
            eventCount = 1,
            comment = "middle"
        )

        assertEquals(
            listOf(latest, middle, old),
            listOf(old, latest, middle).sortedForDisplay()
        )
    }
}
