package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.runtime.Immutable
import epicarchitect.breakbadhabits.database.HabitEventRecord
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Immutable
data class HabitRiskMapData(
    val highlightedCells: Map<Int, Int>,
    val maxCount: Int
)

private const val MinRiskZoneEvents = 2
private const val MaxHighlightedRiskCells = 5
private val RiskMapWindow = 90.days

fun buildHabitRiskMapData(
    records: List<HabitEventRecord>,
    timeZone: TimeZone,
    currentTime: Instant
): HabitRiskMapData? {
    if (records.isEmpty()) return null

    val windowStart = currentTime - RiskMapWindow
    val windowRecords = records.filter { it.startTime >= windowStart }
    if (windowRecords.isEmpty()) return null

    val totalEvents = windowRecords.sumOf { it.eventCount }
    val slipDays = windowRecords
        .map { it.startTime.toLocalDateTime(timeZone).date }
        .toSet()

    if (totalEvents < 5 || slipDays.size < 3) return null

    val cells = IntArray(28)
    val cellDates = Array(28) { mutableSetOf<LocalDate>() }
    windowRecords.forEach { record ->
        val localDateTime = record.startTime.toLocalDateTime(timeZone)
        val day = localDateTime.dayOfWeek.ordinal
        val timeBucket = when (localDateTime.hour) {
            in 6..11 -> 0
            in 12..17 -> 1
            in 18..23 -> 2
            else -> 3
        }
        val cellIndex = timeBucket * 7 + day
        cells[cellIndex] += record.eventCount
        cellDates[cellIndex] += localDateTime.date
    }

    val highlightedCells = cells.indices
        .filter { index -> cells[index] >= MinRiskZoneEvents }
        .sortedWith(
            compareByDescending<Int> { cells[it] }
                .thenByDescending { cellDates[it].size }
                .thenBy { it }
        )
        .take(MaxHighlightedRiskCells)
        .associateWith { cells[it] }

    if (highlightedCells.isEmpty()) return null

    return HabitRiskMapData(
        highlightedCells = highlightedCells,
        maxCount = highlightedCells.values.max()
    )
}
