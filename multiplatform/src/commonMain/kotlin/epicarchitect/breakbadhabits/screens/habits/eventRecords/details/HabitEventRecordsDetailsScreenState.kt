package epicarchitect.breakbadhabits.screens.habits.eventRecords.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.MonthOfYear
import epicarchitect.breakbadhabits.datetime.toLocalDateRange
import epicarchitect.breakbadhabits.datetime.toLocalDateTimeRange
import epicarchitect.breakbadhabits.datetime.toMonthOfYear
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.habits.groupByMonth
import epicarchitect.breakbadhabits.habits.timeRange
import epicarchitect.breakbadhabits.math.ranges.ascended
import epicarchitect.calendar.compose.pager.state.EpicCalendarPagerState
import epicarchitect.calendar.compose.pager.state.rememberEpicCalendarPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.datetime.LocalDate

@Immutable
data class HabitEventRecordsDetailsScreenState(
    val habit: Habit,
    val records: List<HabitEventRecord>,
    val groupedByMonthRecords: Map<MonthOfYear, Set<HabitEventRecord>>,
    val calendarState: EpicCalendarPagerState,
    val calendarRanges: List<ClosedRange<LocalDate>>,
    val currentMonthRecords: List<HabitEventRecord>
)

@Composable
fun rememberHabitEventRecordsDetailsScreenState(
    habitId: Int
): HabitEventRecordsDetailsScreenState? {
    val environment = LocalAppEnvironment.current
    val habitQueries = environment.database.habitQueries
    val habitEventRecordQueries = environment.database.habitEventRecordQueries
    val timeZone = environment.dateTime.currentTimeZone()

    val habitState = remember {
        habitQueries.habitById(habitId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(null)

    val recordsState = remember {
        habitEventRecordQueries.recordsByHabitId(habitId)
            .asFlow()
            .mapToList(Dispatchers.IO)
    }.collectAsState(null)

    val habit = habitState.value ?: return null
    val records = recordsState.value ?: return null

    val epicCalendarState = rememberEpicCalendarPagerState()
    val groupedByMonthRecords = remember(records) { records.groupByMonth(timeZone) }
    val currentMonthRecords = remember(
        epicCalendarState.currentMonth,
        groupedByMonthRecords
    ) {
        val monthIndex = epicCalendarState.currentMonth.toMonthOfYear()
        groupedByMonthRecords[monthIndex]?.toList() ?: emptyList()
    }

    val calendarRanges = remember(records) {
        records.map {
            it.timeRange().toLocalDateTimeRange(timeZone).toLocalDateRange().ascended()
        }
    }

    return remember(
        habit,
        records,
        groupedByMonthRecords,
        currentMonthRecords,
        calendarRanges
    ) {
        HabitEventRecordsDetailsScreenState(
            habit = habit,
            records = records,
            groupedByMonthRecords = groupedByMonthRecords,
            calendarState = epicCalendarState,
            calendarRanges = calendarRanges,
            currentMonthRecords = currentMonthRecords
        )
    }
}