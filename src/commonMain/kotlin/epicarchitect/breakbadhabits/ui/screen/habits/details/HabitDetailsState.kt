package epicarchitect.breakbadhabits.ui.screen.habits.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.HabitEventRecord
import epicarchitect.breakbadhabits.operation.datetime.duration
import epicarchitect.breakbadhabits.operation.datetime.toLocalDateRange
import epicarchitect.breakbadhabits.operation.datetime.toLocalDateTimeRange
import epicarchitect.breakbadhabits.operation.habits.abstinence
import epicarchitect.breakbadhabits.operation.habits.abstinenceRangesByFailedRanges
import epicarchitect.breakbadhabits.operation.habits.failedRanges
import epicarchitect.breakbadhabits.operation.habits.timeRange
import epicarchitect.breakbadhabits.operation.math.ranges.ascended
import epicarchitect.breakbadhabits.ui.component.StatisticData
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.time.Duration

data class HabitDetailsState(
    val abstinenceRanges: List<ClosedRange<Instant>>,
    val abstinence: Duration?,
    val calendarRanges: List<ClosedRange<LocalDate>>,
    val abstinenceHistogramValues: List<Float>,
    val statisticData: List<StatisticData>
)

@Composable
fun rememberHabitDetailsState(
    habitEventRecords: List<HabitEventRecord>,
    lastTrack: HabitEventRecord?,
    currentTime: Instant,
    timeZone: TimeZone
): HabitDetailsState {
    val failedRanges = remember(habitEventRecords) {
        habitEventRecords.failedRanges()
    }
    val abstinenceRanges = remember(failedRanges, habitEventRecords, currentTime) {
        abstinenceRangesByFailedRanges(failedRanges, currentTime)
    }
    val abstinence = remember(lastTrack, currentTime) {
        lastTrack?.abstinence(currentTime)
    }
    val calendarRanges = remember(habitEventRecords, timeZone) {
        habitEventRecords.map {
            it.timeRange.toLocalDateTimeRange(timeZone).toLocalDateRange().ascended()
        }
    }
    val abstinenceHistogramValues = remember(abstinenceRanges) {
        abstinenceRanges.map { it.duration().inWholeSeconds.toFloat() }
    }
    val statisticsData = remember(
        habitEventRecords,
        abstinenceRanges,
        failedRanges,
        currentTime,
        timeZone
    ) {
        habitDetailsStatisticsData(
            habitEventRecords = habitEventRecords,
            abstinenceRanges = abstinenceRanges,
            failedRanges = failedRanges,
            currentTime = currentTime,
            timeZone = timeZone,
            strings = AppData.resources.strings.habitDetailsStrings
        )
    }

    return remember(
        abstinenceRanges,
        abstinence,
        calendarRanges,
        abstinenceHistogramValues,
        statisticsData
    ) {
        HabitDetailsState(
            abstinenceRanges,
            abstinence,
            calendarRanges,
            abstinenceHistogramValues,
            statisticsData
        )
    }
}