package epicarchitect.breakbadhabits.ui.screen.habits.dashboard

import epicarchitect.breakbadhabits.environment.database.HabitEventRecord
import epicarchitect.breakbadhabits.environment.resources.strings.app.AppStrings
import epicarchitect.breakbadhabits.operation.datetime.averageDuration
import epicarchitect.breakbadhabits.operation.datetime.maxDuration
import epicarchitect.breakbadhabits.operation.datetime.minDuration
import epicarchitect.breakbadhabits.operation.datetime.monthOfYear
import epicarchitect.breakbadhabits.operation.datetime.orZero
import epicarchitect.breakbadhabits.operation.datetime.previous
import epicarchitect.breakbadhabits.operation.habits.countEvents
import epicarchitect.breakbadhabits.operation.habits.countEventsInMonth
import epicarchitect.breakbadhabits.operation.habits.habitAbstinenceDurationSinceFirstTrack
import epicarchitect.breakbadhabits.ui.component.StatisticData
import epicarchitect.breakbadhabits.ui.format.DurationFormattingAccuracy
import epicarchitect.breakbadhabits.ui.format.formatted
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

fun habitDetailsStatisticsData(
    habitEventRecords: List<HabitEventRecord>,
    abstinenceRanges: List<ClosedRange<Instant>>,
    failedRanges: List<ClosedRange<Instant>>,
    currentTime: Instant,
    timeZone: TimeZone,
    appStrings: AppStrings
) = if (habitEventRecords.isNotEmpty()) {
    listOf(
        StatisticData(
            name = appStrings.habitDashboardStrings.statisticsAverageAbstinenceTime(),
            value = abstinenceRanges.averageDuration().orZero().formatted(appStrings.durationFormattingStrings, DurationFormattingAccuracy.HOURS)
        ),
        StatisticData(
            name = appStrings.habitDashboardStrings.statisticsMaxAbstinenceTime(),
            value = abstinenceRanges.maxDuration().orZero().formatted(appStrings.durationFormattingStrings, DurationFormattingAccuracy.HOURS)
        ),
        StatisticData(
            name = appStrings.habitDashboardStrings.statisticsMinAbstinenceTime(),
            value = abstinenceRanges.minDuration().orZero().formatted(appStrings.durationFormattingStrings, DurationFormattingAccuracy.HOURS)
        ),
        StatisticData(
            name = appStrings.habitDashboardStrings.statisticsDurationSinceFirstTrack(),
            value = habitAbstinenceDurationSinceFirstTrack(failedRanges, currentTime).orZero()
                .formatted(appStrings.durationFormattingStrings, DurationFormattingAccuracy.HOURS)
        ),
        StatisticData(
            name = appStrings.habitDashboardStrings.statisticsCountEventsInCurrentMonth(),
            value = habitEventRecords.countEventsInMonth(
                monthOfYear = currentTime.monthOfYear(timeZone),
                timeZone = timeZone
            ).toString()
        ),
        StatisticData(
            name = appStrings.habitDashboardStrings.statisticsCountEventsInPreviousMonth(),
            value = habitEventRecords.countEventsInMonth(
                monthOfYear = currentTime.monthOfYear(timeZone).previous(),
                timeZone = timeZone
            ).toString()
        ),
        StatisticData(
            name = appStrings.habitDashboardStrings.statisticsTotalCountEvents(),
            value = habitEventRecords.countEvents().toString()
        )
    )
} else {
    emptyList()
}