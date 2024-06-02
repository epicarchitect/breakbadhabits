package epicarchitect.breakbadhabits.ui.screen.habits.details

import epicarchitect.breakbadhabits.data.HabitTrack
import epicarchitect.breakbadhabits.data.resources.strings.HabitDetailsStrings
import epicarchitect.breakbadhabits.operation.datetime.averageDuration
import epicarchitect.breakbadhabits.operation.datetime.maxDuration
import epicarchitect.breakbadhabits.operation.datetime.minDuration
import epicarchitect.breakbadhabits.operation.datetime.monthOfYear
import epicarchitect.breakbadhabits.operation.datetime.orZero
import epicarchitect.breakbadhabits.operation.datetime.previous
import epicarchitect.breakbadhabits.operation.habits.countEvents
import epicarchitect.breakbadhabits.operation.habits.countEventsInMonth
import epicarchitect.breakbadhabits.operation.habits.habitAbstinenceDurationSinceFirstTrack
import epicarchitect.breakbadhabits.ui.format.DurationFormattingAccuracy
import epicarchitect.breakbadhabits.ui.format.formatted
import epicarchitect.breakbadhabits.ui.component.StatisticData
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

fun habitDetailsStatisticsData(
    habitTracks: List<HabitTrack>,
    abstinenceRanges: List<ClosedRange<Instant>>,
    failedRanges: List<ClosedRange<Instant>>,
    currentTime: Instant,
    timeZone: TimeZone,
    strings: HabitDetailsStrings
) = listOf(
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
        value = habitTracks.countEventsInMonth(
            monthOfYear = currentTime.monthOfYear(timeZone),
            timeZone = timeZone
        ).toString()
    ),
    StatisticData(
        name = strings.statisticsCountEventsInPreviousMonth(),
        value = habitTracks.countEventsInMonth(
            monthOfYear = currentTime.monthOfYear(timeZone).previous(),
            timeZone = timeZone
        ).toString()
    ),
    StatisticData(
        name = strings.statisticsTotalCountEvents(),
        value = habitTracks.countEvents().toString()
    )
)