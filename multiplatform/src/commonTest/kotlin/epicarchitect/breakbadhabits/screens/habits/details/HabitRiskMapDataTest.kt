package epicarchitect.breakbadhabits.screens.habits.details

import epicarchitect.breakbadhabits.database.HabitEventRecord
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HabitRiskMapDataTest {

    private val timeZone = TimeZone.UTC
    private val now = LocalDateTime(2026, 12, 31, 12, 0).toInstant(timeZone)

    @Test
    fun doesNotRequireFourteenDayHistory() {
        val records = listOf(
            record(LocalDateTime(2026, 12, 29, 10, 0)),
            record(LocalDateTime(2026, 12, 29, 11, 0)),
            record(LocalDateTime(2026, 12, 30, 10, 0)),
            record(LocalDateTime(2026, 12, 31, 9, 0)),
            record(LocalDateTime(2026, 12, 31, 10, 0))
        )

        val riskMap = assertNotNull(buildHabitRiskMapData(records, timeZone, now))

        assertEquals(2, riskMap.highlightedCells.size)
    }

    @Test
    fun highlightsRepeatedZone() {
        val friday = LocalDateTime(2026, 12, 11, 20, 0)
        val records = listOf(
            record(friday),
            record(friday),
            record(LocalDateTime(2026, 12, 18, 20, 0)),
            record(LocalDateTime(2026, 12, 18, 20, 30)),
            record(LocalDateTime(2026, 12, 19, 10, 0))
        )

        val riskMap = assertNotNull(buildHabitRiskMapData(records, timeZone, now))
        val fridayEveningIndex = 2 * 7 + friday.dayOfWeek.ordinal

        assertEquals(4, riskMap.highlightedCells[fridayEveningIndex])
        assertEquals(1, riskMap.highlightedCells.size)
    }

    @Test
    fun highlightsSingleDateClusterWhenItHasTwoEvents() {
        val records = listOf(
            record(LocalDateTime(2026, 12, 10, 20, 0)),
            record(LocalDateTime(2026, 12, 10, 20, 30)),
            record(LocalDateTime(2026, 12, 11, 10, 0)),
            record(LocalDateTime(2026, 12, 12, 10, 0)),
            record(LocalDateTime(2026, 12, 13, 10, 0))
        )

        val riskMap = assertNotNull(buildHabitRiskMapData(records, timeZone, now))
        val singleDateIndex = 2 * 7 + LocalDateTime(2026, 12, 10, 20, 0).dayOfWeek.ordinal

        assertEquals(2, riskMap.highlightedCells[singleDateIndex])
        assertEquals(1, riskMap.highlightedCells.size)
    }

    @Test
    fun prefersMoreDistinctDatesWhenCountsTie() {
        val records = listOf(
            record(LocalDateTime(2026, 12, 15, 14, 0)),
            record(LocalDateTime(2026, 12, 22, 14, 0)),
            record(LocalDateTime(2026, 12, 16, 14, 0)),
            record(LocalDateTime(2026, 12, 16, 15, 0)),
            record(LocalDateTime(2026, 12, 17, 10, 0))
        )

        val riskMap = assertNotNull(buildHabitRiskMapData(records, timeZone, now))
        val twoDatesIndex = 1 * 7 + LocalDateTime(2026, 12, 15, 14, 0).dayOfWeek.ordinal
        val oneDateIndex = 1 * 7 + LocalDateTime(2026, 12, 16, 14, 0).dayOfWeek.ordinal

        assertEquals(2, riskMap.highlightedCells[twoDatesIndex])
        assertEquals(2, riskMap.highlightedCells[oneDateIndex])
        assertEquals(
            listOf(twoDatesIndex, oneDateIndex),
            riskMap.highlightedCells.keys.toList()
        )
    }

    @Test
    fun ignoresEventsOlderThanNinetyDays() {
        val oldRecords = listOf(
            record(LocalDateTime(2026, 9, 15, 8, 0), count = 5),
            record(LocalDateTime(2026, 9, 22, 8, 0), count = 5)
        )
        val recentRecords = listOf(
            record(LocalDateTime(2026, 12, 11, 20, 0)),
            record(LocalDateTime(2026, 12, 11, 20, 30)),
            record(LocalDateTime(2026, 12, 18, 20, 0)),
            record(LocalDateTime(2026, 12, 18, 20, 30)),
            record(LocalDateTime(2026, 12, 19, 10, 0))
        )

        val riskMap = assertNotNull(
            buildHabitRiskMapData(oldRecords + recentRecords, timeZone, now)
        )
        val oldMorningIndex = 0 * 7 + LocalDateTime(2026, 9, 15, 8, 0).dayOfWeek.ordinal

        assertFalse(oldMorningIndex in riskMap.highlightedCells)
    }

    @Test
    fun keepsOnlyTopFiveZones() {
        val monday = LocalDate(2026, 11, 2)
        val records = (0..5).flatMap { dayOffset ->
            val firstDate = monday.plus(DatePeriod(days = dayOffset))
            val secondDate = firstDate.plus(DatePeriod(days = 7))
            listOf(
                record(LocalDateTime(firstDate.year, firstDate.monthNumber, firstDate.dayOfMonth, 20, 0)),
                record(LocalDateTime(secondDate.year, secondDate.monthNumber, secondDate.dayOfMonth, 20, 0))
            )
        }

        val riskMap = assertNotNull(buildHabitRiskMapData(records, timeZone, now))
        val eveningIndices = (14..19).toList()

        assertEquals(5, riskMap.highlightedCells.size)
        assertTrue(riskMap.highlightedCells.keys.containsAll(eveningIndices.take(5)))
        assertFalse(eveningIndices.last() in riskMap.highlightedCells)
    }

    @Test
    fun includesEventsExactlyAtNinetyDayBoundary() {
        val boundaryDateTime = (now - 90.days).toLocalDateTime(timeZone)
        val records = listOf(
            record(boundaryDateTime, count = 2),
            record(LocalDateTime(2026, 11, 10, 10, 0)),
            record(LocalDateTime(2026, 11, 11, 10, 0)),
            record(LocalDateTime(2026, 11, 12, 10, 0))
        )

        val riskMap = assertNotNull(buildHabitRiskMapData(records, timeZone, now))
        val boundaryIndex = 1 * 7 + boundaryDateTime.dayOfWeek.ordinal

        assertEquals(2, riskMap.highlightedCells[boundaryIndex])
    }

    @Test
    fun excludesEventsJustBeforeNinetyDayBoundary() {
        val oldDateTime = (now - 90.days - 1.seconds).toLocalDateTime(timeZone)
        val records = listOf(
            record(oldDateTime, count = 100),
            record(LocalDateTime(2026, 12, 11, 20, 0)),
            record(LocalDateTime(2026, 12, 11, 20, 30)),
            record(LocalDateTime(2026, 12, 18, 20, 0)),
            record(LocalDateTime(2026, 12, 18, 20, 30)),
            record(LocalDateTime(2026, 12, 19, 10, 0))
        )

        val riskMap = assertNotNull(buildHabitRiskMapData(records, timeZone, now))
        val oldIndex = 0 * 7 + oldDateTime.dayOfWeek.ordinal

        assertFalse(oldIndex in riskMap.highlightedCells)
    }

    @Test
    fun usesSixAsMorningAndMidnightAsNight() {
        val records = listOf(
            record(LocalDateTime(2026, 12, 7, 5, 59)),
            record(LocalDateTime(2026, 12, 14, 0, 0)),
            record(LocalDateTime(2026, 12, 8, 6, 0)),
            record(LocalDateTime(2026, 12, 15, 6, 0)),
            record(LocalDateTime(2026, 12, 9, 23, 59)),
            record(LocalDateTime(2026, 12, 16, 23, 59))
        )

        val riskMap = assertNotNull(buildHabitRiskMapData(records, timeZone, now))
        val nightIndex = 3 * 7 + LocalDateTime(2026, 12, 7, 5, 59).dayOfWeek.ordinal
        val morningIndex = 0 * 7 + LocalDateTime(2026, 12, 8, 6, 0).dayOfWeek.ordinal
        val eveningIndex = 2 * 7 + LocalDateTime(2026, 12, 9, 23, 59).dayOfWeek.ordinal

        assertEquals(2, riskMap.highlightedCells[nightIndex])
        assertEquals(2, riskMap.highlightedCells[morningIndex])
        assertEquals(2, riskMap.highlightedCells[eveningIndex])
        assertFalse(1 * 7 + LocalDateTime(2026, 12, 7, 5, 59).dayOfWeek.ordinal in riskMap.highlightedCells)
    }

    @Test
    fun usesTwelveAsDayAndEighteenAsEvening() {
        val records = listOf(
            record(LocalDateTime(2026, 12, 7, 11, 59)),
            record(LocalDateTime(2026, 12, 14, 11, 59)),
            record(LocalDateTime(2026, 12, 8, 12, 0)),
            record(LocalDateTime(2026, 12, 15, 17, 59)),
            record(LocalDateTime(2026, 12, 9, 18, 0)),
            record(LocalDateTime(2026, 12, 16, 18, 0))
        )

        val riskMap = assertNotNull(buildHabitRiskMapData(records, timeZone, now))
        val morningIndex = 0 * 7 + LocalDateTime(2026, 12, 7, 11, 59).dayOfWeek.ordinal
        val dayIndex = 1 * 7 + LocalDateTime(2026, 12, 8, 12, 0).dayOfWeek.ordinal
        val eveningIndex = 2 * 7 + LocalDateTime(2026, 12, 9, 18, 0).dayOfWeek.ordinal

        assertEquals(2, riskMap.highlightedCells[morningIndex])
        assertEquals(2, riskMap.highlightedCells[dayIndex])
        assertEquals(2, riskMap.highlightedCells[eveningIndex])
        assertFalse(3 * 7 + LocalDateTime(2026, 12, 7, 11, 59).dayOfWeek.ordinal in riskMap.highlightedCells)
    }

    @Test
    fun usesLocalDateNearMidnight() {
        val zone = TimeZone.of("Pacific/Pago_Pago")
        val zoneNow = LocalDateTime(2026, 12, 20, 12, 0).toInstant(zone)
        val lateEvening = LocalDateTime(2026, 12, 10, 23, 30)
        val records = listOf(
            recordAt(lateEvening, zone),
            recordAt(lateEvening, zone),
            recordAt(LocalDateTime(2026, 12, 11, 10, 0), zone),
            recordAt(LocalDateTime(2026, 12, 12, 10, 0), zone),
            recordAt(LocalDateTime(2026, 12, 13, 10, 0), zone)
        )

        val riskMap = assertNotNull(buildHabitRiskMapData(records, zone, zoneNow))
        val expectedIndex = 2 * 7 + lateEvening.dayOfWeek.ordinal

        assertEquals(2, riskMap.highlightedCells[expectedIndex])
    }

    @Test
    fun usesLocalTimeAcrossDaylightSavingTransition() {
        val zone = TimeZone.of("America/New_York")
        val zoneNow = LocalDateTime(2026, 3, 15, 12, 0).toInstant(zone)
        val beforeTransition = LocalDateTime(2026, 3, 8, 1, 30)
        val afterTransition = LocalDateTime(2026, 3, 8, 3, 30)
        val records = listOf(
            recordAt(beforeTransition, zone),
            recordAt(afterTransition, zone),
            recordAt(LocalDateTime(2026, 3, 9, 10, 0), zone),
            recordAt(LocalDateTime(2026, 3, 10, 10, 0), zone),
            recordAt(LocalDateTime(2026, 3, 11, 10, 0), zone)
        )

        val riskMap = assertNotNull(buildHabitRiskMapData(records, zone, zoneNow))
        val expectedIndex = 3 * 7 + beforeTransition.dayOfWeek.ordinal

        assertEquals(2, riskMap.highlightedCells[expectedIndex])
    }

    @Test
    fun returnsNullWithoutFiveEvents() {
        val records = listOf(
            record(LocalDateTime(2026, 12, 10, 20, 0)),
            record(LocalDateTime(2026, 12, 10, 20, 30)),
            record(LocalDateTime(2026, 12, 11, 10, 0)),
            record(LocalDateTime(2026, 12, 12, 10, 0))
        )

        assertNull(buildHabitRiskMapData(records, timeZone, now))
    }

    @Test
    fun returnsNullWithoutThreeSlipDays() {
        val records = listOf(
            record(LocalDateTime(2026, 12, 10, 20, 0)),
            record(LocalDateTime(2026, 12, 10, 20, 30)),
            record(LocalDateTime(2026, 12, 11, 20, 0)),
            record(LocalDateTime(2026, 12, 11, 20, 30)),
            record(LocalDateTime(2026, 12, 11, 21, 0))
        )

        assertNull(buildHabitRiskMapData(records, timeZone, now))
    }

    @Test
    fun returnsNullWhenNoZoneHasTwoEvents() {
        val records = (7..11).map { day ->
            record(LocalDateTime(2026, 12, day, 20, 0))
        }

        assertNull(buildHabitRiskMapData(records, timeZone, now))
    }

    @Test
    fun returnsNullWhenAllEventsAreOutsideWindow() {
        val records = listOf(
            record(LocalDateTime(2026, 9, 15, 8, 0)),
            record(LocalDateTime(2026, 9, 15, 9, 0)),
            record(LocalDateTime(2026, 9, 22, 8, 0))
        )

        assertNull(buildHabitRiskMapData(records, timeZone, now))
    }

    private fun record(dateTime: LocalDateTime, count: Int = 1) =
        recordAt(dateTime, timeZone, count)

    private fun recordAt(
        dateTime: LocalDateTime,
        zone: TimeZone,
        count: Int = 1
    ): HabitEventRecord {
        val instant = dateTime.toInstant(zone)
        return HabitEventRecord(
            id = Uuid.random(),
            habitId = Uuid.random(),
            startTime = instant,
            endTime = instant + 1.hours,
            eventCount = count,
            comment = ""
        )
    }
}
