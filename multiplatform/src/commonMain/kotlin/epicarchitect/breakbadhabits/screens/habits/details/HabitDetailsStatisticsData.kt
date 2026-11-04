package epicarchitect.breakbadhabits.screens.habits.details

import androidx.compose.runtime.Composable
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.averageDuration
import epicarchitect.breakbadhabits.datetime.maxDuration
import epicarchitect.breakbadhabits.datetime.minDuration
import epicarchitect.breakbadhabits.datetime.monthOfYear
import epicarchitect.breakbadhabits.datetime.orZero
import epicarchitect.breakbadhabits.datetime.previous
import epicarchitect.breakbadhabits.format.Accuracy
import epicarchitect.breakbadhabits.format.formatted
import epicarchitect.breakbadhabits.habits.countEvents
import epicarchitect.breakbadhabits.habits.countEventsInMonth
import epicarchitect.breakbadhabits.habits.habitAbstinenceDurationSinceFirstTrack
import epicarchitect.breakbadhabits.resources.Res
import epicarchitect.breakbadhabits.resources.statistics_average_abstinence_time
import epicarchitect.breakbadhabits.resources.statistics_duration_since_first_event
import epicarchitect.breakbadhabits.resources.statistics_events_in_current_month
import epicarchitect.breakbadhabits.resources.statistics_events_in_previous_month
import epicarchitect.breakbadhabits.resources.statistics_max_abstinence_time
import epicarchitect.breakbadhabits.resources.statistics_min_abstinence_time
import epicarchitect.breakbadhabits.resources.statistics_total_events
import epicarchitect.breakbadhabits.uikit.StatisticData
import kotlinx.datetime.TimeZone
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@Composable
fun habitDetailsStatisticsData(
    habitEventRecords: List<HabitEventRecord>,
    abstinenceRanges: List<ClosedRange<Instant>>,
    failedRanges: List<ClosedRange<Instant>>,
    currentTime: Instant
): List<StatisticData> {
    val timeZone = TimeZone.currentSystemDefault()

    return if (habitEventRecords.isNotEmpty()) {
        listOf(
            StatisticData(
                name = stringResource(Res.string.statistics_average_abstinence_time),
                value = abstinenceRanges.averageDuration().orZero().formatted(Accuracy.HOURS)
            ),
            StatisticData(
                name = stringResource(Res.string.statistics_max_abstinence_time),
                value = abstinenceRanges.maxDuration().orZero().formatted(Accuracy.HOURS)
            ),
            StatisticData(
                name = stringResource(Res.string.statistics_min_abstinence_time),
                value = abstinenceRanges.minDuration().orZero().formatted(Accuracy.HOURS)
            ),
            StatisticData(
                name = stringResource(Res.string.statistics_duration_since_first_event),
                value = habitAbstinenceDurationSinceFirstTrack(
                    failedRanges = failedRanges,
                    currentTime = currentTime
                ).orZero().formatted(Accuracy.HOURS)
            ),
            StatisticData(
                name = stringResource(Res.string.statistics_events_in_current_month),
                value = habitEventRecords.countEventsInMonth(
                    monthOfYear = currentTime.monthOfYear(timeZone),
                    timeZone = timeZone
                ).formatted()
            ),
            StatisticData(
                name = stringResource(Res.string.statistics_events_in_previous_month),
                value = habitEventRecords.countEventsInMonth(
                    monthOfYear = currentTime.monthOfYear(timeZone).previous(),
                    timeZone = timeZone
                ).formatted()
            ),
            StatisticData(
                name = stringResource(Res.string.statistics_total_events),
                value = habitEventRecords.countEvents().formatted()
            )
        )
    } else {
        emptyList()
    }
}
