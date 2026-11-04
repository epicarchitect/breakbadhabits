package epicarchitect.breakbadhabits.screens.habits.details

import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import kotlinx.datetime.LocalDate
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.uuid.Uuid

internal val previewHabit = Habit(
    id = Uuid.random(),
    name = "Курение",
    iconId = 0
)

internal fun previewHabitState(): HabitDetailsScreenState {
    val now = Clock.System.now()
    return HabitDetailsScreenState(
        habit = previewHabit,
        habitEventRecords = listOf(
            HabitEventRecord(
                id = Uuid.random(),
                habitId = previewHabit.id,
                startTime = now - 20.days,
                endTime = now - 20.days + 2.hours,
                eventCount = 1,
                comment = "После кофе"
            )
        ),
        abstinence = 34.days + 4.hours,
        calendarRanges = listOf(
            LocalDate(2026, 11, 3)..LocalDate(2026, 11, 4),
            LocalDate(2026, 11, 12)..LocalDate(2026, 11, 13)
        ),
        abstinenceHistogramValues = listOf(1f, 2f, 3f, 2.5f, 4f),
        riskMap = HabitRiskMapData(
            highlightedCells = mapOf(9 to 3, 12 to 2, 20 to 1),
            maxCount = 3
        )
    )
}
