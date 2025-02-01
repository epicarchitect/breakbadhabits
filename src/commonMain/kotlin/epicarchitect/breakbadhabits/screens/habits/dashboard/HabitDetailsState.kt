package epicarchitect.breakbadhabits.screens.habits.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.database.AppSettings
import epicarchitect.breakbadhabits.database.Habit
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.duration
import epicarchitect.breakbadhabits.datetime.toLocalDateRange
import epicarchitect.breakbadhabits.datetime.toLocalDateTimeRange
import epicarchitect.breakbadhabits.habits.abstinence
import epicarchitect.breakbadhabits.habits.failedRanges
import epicarchitect.breakbadhabits.habits.gamification.HabitGamificationData
import epicarchitect.breakbadhabits.habits.gamification.habitGamificationData
import epicarchitect.breakbadhabits.habits.habitAbstinenceRangesByFailedRanges
import epicarchitect.breakbadhabits.habits.timeRange
import epicarchitect.breakbadhabits.math.ranges.ascended
import epicarchitect.breakbadhabits.uikit.StatisticData
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlin.time.Duration

data class HabitDetailsState(
    val abstinenceRanges: List<ClosedRange<Instant>>,
    val abstinence: Duration?,
    val calendarRanges: List<ClosedRange<LocalDate>>,
    val abstinenceHistogramValues: List<Float>,
    val statisticData: List<StatisticData>,
    val gamificationEnabled: Boolean,
    val gamificationData: HabitGamificationData?
)

@Composable
fun rememberHabitDetailsState(
    habit: Habit,
    habitEventRecords: List<HabitEventRecord>,
    appSettings: AppSettings,
    lastTrack: HabitEventRecord?,
    currentTime: Instant,
    timeZone: TimeZone
): HabitDetailsState {
    val failedRanges = remember(habitEventRecords) {
        habitEventRecords.failedRanges()
    }
    val abstinenceRanges = remember(failedRanges, habitEventRecords, currentTime) {
        habitAbstinenceRangesByFailedRanges(failedRanges, currentTime)
    }
    val abstinence = remember(lastTrack, currentTime) {
        lastTrack?.abstinence(currentTime)
    }
    val calendarRanges = remember(habitEventRecords, timeZone) {
        habitEventRecords.map {
            it.timeRange().toLocalDateTimeRange(timeZone).toLocalDateRange().ascended()
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
            timeZone = timeZone
        )
    }

    val gamificationData = remember(
        habit,
        currentTime,
        appSettings.gamificationEnabled,
        abstinence
    ) {
        if (appSettings.gamificationEnabled && abstinence != null) {
            habitGamificationData(
                habit = habit,
                level = Environment.habitLevels.get(habit.level),
                abstinence = abstinence
            )
        } else {
            null
        }
    }

    return remember(
        abstinenceRanges,
        abstinence,
        calendarRanges,
        abstinenceHistogramValues,
        statisticsData,
        appSettings.gamificationEnabled,
        gamificationData
    ) {
        HabitDetailsState(
            abstinenceRanges = abstinenceRanges,
            abstinence = abstinence,
            calendarRanges = calendarRanges,
            abstinenceHistogramValues = abstinenceHistogramValues,
            statisticData = statisticsData,
            gamificationEnabled = appSettings.gamificationEnabled,
            gamificationData = gamificationData
        )
    }
}