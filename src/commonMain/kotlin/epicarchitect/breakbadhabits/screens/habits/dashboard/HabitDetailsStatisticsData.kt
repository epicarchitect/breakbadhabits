package epicarchitect.breakbadhabits.screens.habits.dashboard

import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.uikit.StatisticData
import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.averageDuration
import epicarchitect.breakbadhabits.datetime.format.DurationFormattingAccuracy
import epicarchitect.breakbadhabits.datetime.format.formatted
import epicarchitect.breakbadhabits.datetime.maxDuration
import epicarchitect.breakbadhabits.datetime.minDuration
import epicarchitect.breakbadhabits.datetime.monthOfYear
import epicarchitect.breakbadhabits.datetime.orZero
import epicarchitect.breakbadhabits.datetime.previous
import epicarchitect.breakbadhabits.habits.countEvents
import epicarchitect.breakbadhabits.habits.countEventsInMonth
import epicarchitect.breakbadhabits.habits.habitAbstinenceDurationSinceFirstTrack
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

fun habitDetailsStatisticsData(
    habitEventRecords: List<HabitEventRecord>,
    abstinenceRanges: List<ClosedRange<Instant>>,
    failedRanges: List<ClosedRange<Instant>>,
    currentTime: Instant,
    timeZone: TimeZone
) = if (habitEventRecords.isNotEmpty()) {
    val strings = Environment.resources.strings.habitDashboardStrings
    listOf(
        StatisticData(
            name = strings.statisticsAverageAbstinenceTime(),
            value = abstinenceRanges.averageDuration().orZero().formatted(DurationFormattingAccuracy.HOURS)
        ),
        StatisticData(
            name = strings.statisticsMaxAbstinenceTime(),
            value = abstinenceRanges.maxDuration().orZero().formatted(DurationFormattingAccuracy.HOURS)
        ),
        StatisticData(
            name = strings.statisticsMinAbstinenceTime(),
            value = abstinenceRanges.minDuration().orZero().formatted(DurationFormattingAccuracy.HOURS)
        ),
        StatisticData(
            name = strings.statisticsDurationSinceFirstTrack(),
            value = habitAbstinenceDurationSinceFirstTrack(failedRanges, currentTime).orZero()
                .formatted(DurationFormattingAccuracy.HOURS)
        ),
        StatisticData(
            name = strings.statisticsCountEventsInCurrentMonth(),
            value = habitEventRecords.countEventsInMonth(
                monthOfYear = currentTime.monthOfYear(timeZone),
                timeZone = timeZone
            ).toString()
        ),
        StatisticData(
            name = strings.statisticsCountEventsInPreviousMonth(),
            value = habitEventRecords.countEventsInMonth(
                monthOfYear = currentTime.monthOfYear(timeZone).previous(),
                timeZone = timeZone
            ).toString()
        ),
        StatisticData(
            name = strings.statisticsTotalCountEvents(),
            value = habitEventRecords.countEvents().toString()
        )
    )
} else {
    emptyList()
}