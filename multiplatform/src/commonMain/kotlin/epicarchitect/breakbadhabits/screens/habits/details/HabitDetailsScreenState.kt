package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.duration
import epicarchitect.breakbadhabits.datetime.toLocalDateRange
import epicarchitect.breakbadhabits.datetime.toLocalDateTimeRange
import epicarchitect.breakbadhabits.habits.HabitsTimePulse
import epicarchitect.breakbadhabits.habits.abstinence
import epicarchitect.breakbadhabits.habits.failedRanges
import epicarchitect.breakbadhabits.habits.habitAbstinenceRangesByFailedRanges
import epicarchitect.breakbadhabits.habits.timeRange
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.math.ranges.ascended
import epicarchitect.breakbadhabits.uikit.StatisticData
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.time.Duration
import kotlin.uuid.Uuid

@Immutable
data class HabitDetailsScreenState(
    val habit: Habit,
    val habitEventRecords: List<HabitEventRecord>,
    val abstinence: Duration?,
    val calendarRanges: List<ClosedRange<LocalDate>>,
    val abstinenceHistogramValues: List<Float>,
    val statistics: List<StatisticData>
)

@Composable
fun rememberHabitDetailsScreenState(habitId: Uuid): HabitDetailsScreenState? {
    val habitsDao = AppDatabase.habitsDao
    val habitEventRecordsDao = AppDatabase.habitEventRecordsDao
    val currentTime by HabitsTimePulse.state.collectAsState()
    val timeZone = TimeZone.currentSystemDefault()

    val habitState = remember(habitId) {
        habitsDao.habitById(habitId)
    }.collectAsState(null)

    val eventRecordsState = remember(habitId) {
        habitEventRecordsDao.recordsByHabitId(habitId)
    }.collectAsState(null)

    val lastRecordState = remember(habitId) {
        habitEventRecordsDao.recordByHabitIdAndMaxEndTime(habitId)
    }.collectAsState(null)

    val habit = habitState.value ?: return null
    val eventRecords = eventRecordsState.value ?: return null
    val lastRecord = lastRecordState.value

    val failedRanges = remember(eventRecords) {
        eventRecords.failedRanges()
    }
    val abstinenceRanges = remember(failedRanges, eventRecords, currentTime) {
        habitAbstinenceRangesByFailedRanges(failedRanges, currentTime)
    }
    val abstinence = remember(lastRecord, currentTime) {
        lastRecord?.abstinence(currentTime)
    }
    val calendarRanges = remember(eventRecords, timeZone) {
        eventRecords.map {
            it.timeRange().toLocalDateTimeRange(timeZone).toLocalDateRange().ascended()
        }
    }
    val abstinenceHistogramValues = remember(abstinenceRanges) {
        abstinenceRanges.map { it.duration().inWholeSeconds.toFloat() }
    }

    val statisticsData = habitDetailsStatisticsData(
        habitEventRecords = eventRecords,
        abstinenceRanges = abstinenceRanges,
        failedRanges = failedRanges,
        currentTime = currentTime
    )

    return remember(
        habit,
        eventRecords,
        abstinence,
        calendarRanges,
        abstinenceHistogramValues,
        statisticsData
    ) {
        HabitDetailsScreenState(
            habit = habit,
            habitEventRecords = eventRecords,
            abstinence = abstinence,
            calendarRanges = calendarRanges,
            abstinenceHistogramValues = abstinenceHistogramValues,
            statistics = statisticsData
        )
    }
}
