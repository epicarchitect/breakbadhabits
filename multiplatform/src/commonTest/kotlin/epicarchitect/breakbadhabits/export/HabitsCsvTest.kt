package epicarchitect.breakbadhabits.export

import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Instant
import kotlin.uuid.Uuid

class HabitsCsvTest {

    private val timeZone = TimeZone.UTC
    private val start = Instant.fromEpochSeconds(1_700_000_000)
    private val end = Instant.fromEpochSeconds(1_700_003_600)

    @Test
    fun roundTripPreservesHabitsEventsAndEscapedComments() {
        val habit = Habit(
            id = Uuid.random(),
            name = "Habit; \"A\"",
            iconId = 3
        )
        val record = HabitEventRecord(
            id = Uuid.random(),
            habitId = habit.id,
            startTime = start,
            endTime = end,
            eventCount = 4,
            comment = "line; with \"quotes\"\nand newline"
        )

        val csv = buildHabitsCsv(
            habits = listOf(habit),
            eventRecords = listOf(record),
            timeZone = timeZone
        )
        val parsed = parseHabitsCsv(csv, timeZone) ?: error("CSV must parse")

        assertEquals(1, parsed.size)
        assertEquals(habit.name, parsed.single().name)
        assertEquals(habit.iconId, parsed.single().iconId)
        assertEquals(1, parsed.single().records.size)
        assertEquals(start, parsed.single().records.single().startTime)
        assertEquals(end, parsed.single().records.single().endTime)
        assertEquals(record.eventCount, parsed.single().records.single().eventCount)
        assertEquals(record.comment, parsed.single().records.single().comment)
    }

    @Test
    fun habitWithoutRecordsSurvivesRoundTrip() {
        val habit = Habit(
            id = Uuid.random(),
            name = "Empty habit",
            iconId = 5
        )

        val csv = buildHabitsCsv(
            habits = listOf(habit),
            eventRecords = emptyList(),
            timeZone = timeZone
        )
        val parsed = parseHabitsCsv(csv, timeZone) ?: error("CSV must parse")

        assertEquals(1, parsed.size)
        assertEquals(habit.name, parsed.single().name)
        assertEquals(emptyList(), parsed.single().records)
    }

    @Test
    fun unknownIconFallsBackToFirstIcon() {
        val habit = Habit(
            id = Uuid.random(),
            name = "Unknown icon",
            iconId = 999
        )

        val csv = buildHabitsCsv(
            habits = listOf(habit),
            eventRecords = emptyList(),
            timeZone = timeZone
        )
        val parsed = parseHabitsCsv(csv, timeZone) ?: error("CSV must parse")

        assertEquals(0, parsed.single().iconId)
    }

    @Test
    fun recordWithEndBeforeStartIsRejected() {
        val habit = Habit(
            id = Uuid.random(),
            name = "Broken range",
            iconId = 0
        )
        val record = HabitEventRecord(
            id = Uuid.random(),
            habitId = habit.id,
            startTime = end,
            endTime = start,
            eventCount = 1,
            comment = ""
        )

        val csv = buildHabitsCsv(
            habits = listOf(habit),
            eventRecords = listOf(record),
            timeZone = timeZone
        )

        assertNull(parseHabitsCsv(csv, timeZone))
    }
}
