package epicarchitect.breakbadhabits.screens.habits.eventRecords.details

import epicarchitect.breakbadhabits.infrastructure.AppTimeZone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.toLocalDateRange
import epicarchitect.breakbadhabits.datetime.toLocalDateTimeRange
import epicarchitect.breakbadhabits.datetime.toMonthOfYear
import epicarchitect.breakbadhabits.habits.groupByMonth
import epicarchitect.breakbadhabits.habits.timeRange
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.math.ranges.ascended
import epicarchitect.calendar.compose.pager.state.EpicCalendarPagerState
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

@Immutable
data class HabitEventRecordsDetailsScreenState(
    val habit: Habit,
    val calendarState: EpicCalendarPagerState,
    val calendarRanges: List<ClosedRange<LocalDate>>,
    val currentMonthRecords: List<HabitEventRecord>
)

internal fun Iterable<HabitEventRecord>.sortedForDisplay(): List<HabitEventRecord> =
    sortedWith(
        compareByDescending<HabitEventRecord> { it.startTime }
            .thenByDescending { it.endTime }
    )

@Composable
fun rememberHabitEventRecordsDetailsScreenState(
    habitId: Uuid
): HabitEventRecordsDetailsScreenState? {

    val habitState = remember {
        AppDatabase.habitsDao.habitById(habitId)
    }.collectAsState(null)

    val recordsState = remember(habitId) {
        AppDatabase.habitEventRecordsDao.recordsByHabitId(habitId)
    }.collectAsState(null)

    val habit = habitState.value ?: return null
    val records = recordsState.value ?: return null

    val epicCalendarState = rememberEpicCalendarPagerState()
    val groupedByMonthRecords = remember(records) { records.groupByMonth(AppTimeZone) }
    val currentMonthRecords = remember(
        epicCalendarState.currentMonth,
        groupedByMonthRecords
    ) {
        val monthIndex = epicCalendarState.currentMonth.toMonthOfYear()
        groupedByMonthRecords[monthIndex]?.sortedForDisplay().orEmpty()
    }

    val calendarRanges = remember(records) {
        records.map {
            it.timeRange().toLocalDateTimeRange(AppTimeZone).toLocalDateRange().ascended()
        }
    }

    return remember(
        habit,
        currentMonthRecords,
        calendarRanges
    ) {
        HabitEventRecordsDetailsScreenState(
            habit = habit,
            calendarState = epicCalendarState,
            calendarRanges = calendarRanges,
            currentMonthRecords = currentMonthRecords
        )
    }
}
