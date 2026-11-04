package epicarchitect.breakbadhabits.screens.habits.details

import epicarchitect.breakbadhabits.infrastructure.Ticks
import epicarchitect.breakbadhabits.infrastructure.AppTimeZone
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
import epicarchitect.breakbadhabits.habits.abstinence
import epicarchitect.breakbadhabits.habits.failedRanges
import epicarchitect.breakbadhabits.habits.habitAbstinenceRangesByFailedRanges
import epicarchitect.breakbadhabits.habits.timeRange
import epicarchitect.breakbadhabits.infrastructure.AppDatabase
import epicarchitect.breakbadhabits.math.ranges.ascended
import kotlinx.datetime.LocalDate
import kotlin.time.Duration
import kotlin.uuid.Uuid

@Immutable
data class HabitDetailsScreenState(
    val habit: Habit,
    val habitEventRecords: List<HabitEventRecord>,
    val abstinence: Duration?,
    val calendarRanges: List<ClosedRange<LocalDate>>,
    val abstinenceHistogramValues: List<Float>,
    val riskMap: HabitRiskMapData?
)

@Composable
fun rememberHabitDetailsScreenState(habitId: Uuid): HabitDetailsScreenState? {
    val now by Ticks.collectAsState()

    val habitState = remember(habitId) {
        AppDatabase.habitsDao.habitById(habitId)
    }.collectAsState(null)

    val eventRecordsState = remember(habitId) {
        AppDatabase.habitEventRecordsDao.recordsByHabitId(habitId)
    }.collectAsState(null)

    val lastRecordState = remember(habitId) {
        AppDatabase.habitEventRecordsDao.recordByHabitIdAndMaxEndTime(habitId)
    }.collectAsState(null)

    val habit = habitState.value ?: return null
    val eventRecords = eventRecordsState.value ?: return null
    val lastRecord = lastRecordState.value

    val failedRanges = remember(eventRecords) {
        eventRecords.failedRanges()
    }
    val abstinenceRanges = remember(failedRanges, eventRecords, now) {
        habitAbstinenceRangesByFailedRanges(failedRanges, now)
    }
    val abstinence = remember(lastRecord, now) {
        lastRecord?.abstinence(now)
    }
    val calendarRanges = remember(eventRecords, AppTimeZone) {
        eventRecords.map {
            it.timeRange().toLocalDateTimeRange(AppTimeZone).toLocalDateRange().ascended()
        }
    }
    val abstinenceHistogramValues = remember(abstinenceRanges) {
        abstinenceRanges.map { it.duration().inWholeSeconds.toFloat() }
    }

    val riskMap = remember(eventRecords, AppTimeZone, now) {
        buildHabitRiskMapData(
            records = eventRecords,
            timeZone = AppTimeZone,
            currentTime = now
        )
    }

    return remember(
        habit,
        eventRecords,
        abstinence,
        calendarRanges,
        abstinenceHistogramValues,
        riskMap
    ) {
        HabitDetailsScreenState(
            habit = habit,
            habitEventRecords = eventRecords,
            abstinence = abstinence,
            calendarRanges = calendarRanges,
            abstinenceHistogramValues = abstinenceHistogramValues,
            riskMap = riskMap
        )
    }
}
