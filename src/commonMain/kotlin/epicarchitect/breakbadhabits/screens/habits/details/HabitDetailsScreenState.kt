package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.duration
import epicarchitect.breakbadhabits.datetime.toLocalDateRange
import epicarchitect.breakbadhabits.datetime.toLocalDateTimeRange
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.habits.abstinence
import epicarchitect.breakbadhabits.habits.failedRanges
import epicarchitect.breakbadhabits.habits.habitAbstinenceRangesByFailedRanges
import epicarchitect.breakbadhabits.habits.timeRange
import epicarchitect.breakbadhabits.math.ranges.ascended
import epicarchitect.breakbadhabits.uikit.StatisticData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.time.Duration

@Immutable
data class HabitDetailsScreenState(
    val habit: Habit,
    val habitEventRecords: List<HabitEventRecord>,
    val abstinenceRanges: List<ClosedRange<Instant>>,
    val abstinence: Duration?,
    val calendarRanges: List<ClosedRange<LocalDate>>,
    val abstinenceHistogramValues: List<Float>,
    val statistics: List<StatisticData>
)

@Composable
fun rememberHabitDetailsScreenState(habitId: Int): HabitDetailsScreenState? {
    val environment = LocalAppEnvironment.current
    val habitQueries = environment.database.habitQueries
    val eventRecordQueries = environment.database.habitEventRecordQueries
    val currentTime by environment.habits.timePulse.state.collectAsState()
    val timeZone = environment.dateTime.currentTimeZone()

    val habitState = remember(habitId) {
        habitQueries.habitById(habitId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(null)

    val eventRecordsState = remember(habitId) {
        eventRecordQueries.recordsByHabitId(habitId)
            .asFlow()
            .mapToList(Dispatchers.IO)
    }.collectAsState(null)

    val lastRecordState = remember(habitId) {
        eventRecordQueries.recordByHabitIdAndMaxEndTime(habitId)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
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

    val statisticsData = remember(
        eventRecords,
        abstinenceRanges,
        failedRanges,
        currentTime
    ) {
        habitDetailsStatisticsData(
            environment = environment,
            habitEventRecords = eventRecords,
            abstinenceRanges = abstinenceRanges,
            failedRanges = failedRanges,
            currentTime = currentTime
        )
    }

    return remember(
        habit,
        eventRecords,
        abstinenceRanges,
        abstinence,
        calendarRanges,
        abstinenceHistogramValues,
        statisticsData
    ) {
        HabitDetailsScreenState(
            habit,
            eventRecords,
            abstinenceRanges,
            abstinence,
            calendarRanges,
            abstinenceHistogramValues,
            statisticsData
        )
    }
}