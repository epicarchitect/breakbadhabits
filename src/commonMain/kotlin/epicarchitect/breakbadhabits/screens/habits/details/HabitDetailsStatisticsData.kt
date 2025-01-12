package epicarchitect.breakbadhabits.screens.habits.details

import epicarchitect.breakbadhabits.database.HabitEventRecord
import epicarchitect.breakbadhabits.datetime.averageDuration
import epicarchitect.breakbadhabits.datetime.maxDuration
import epicarchitect.breakbadhabits.datetime.minDuration
import epicarchitect.breakbadhabits.datetime.monthOfYear
import epicarchitect.breakbadhabits.datetime.orZero
import epicarchitect.breakbadhabits.datetime.previous
import epicarchitect.breakbadhabits.environment.AppEnvironment
import epicarchitect.breakbadhabits.format.DurationFormatter
import epicarchitect.breakbadhabits.habits.countEvents
import epicarchitect.breakbadhabits.habits.countEventsInMonth
import epicarchitect.breakbadhabits.habits.habitAbstinenceDurationSinceFirstTrack
import epicarchitect.breakbadhabits.uikit.StatisticData
import kotlinx.datetime.Instant

fun habitDetailsStatisticsData(
    environment: AppEnvironment,
    habitEventRecords: List<HabitEventRecord>,
    abstinenceRanges: List<ClosedRange<Instant>>,
    failedRanges: List<ClosedRange<Instant>>,
    currentTime: Instant
): List<StatisticData> {
    val strings = environment.resources.strings.habitDashboardStrings
    val durationFormatter = environment.format.durationFormatter
    val numberFormatter = environment.format.numberFormatter
    val timeZone = environment.dateTime.currentTimeZone()

    return if (habitEventRecords.isNotEmpty()) {
        listOf(
            StatisticData(
                name = strings.statisticsAverageAbstinenceTime(),
                value = durationFormatter.format(
                    duration = abstinenceRanges.averageDuration().orZero(),
                    accuracy = DurationFormatter.Accuracy.HOURS
                )
            ),
            StatisticData(
                name = strings.statisticsMaxAbstinenceTime(),
                value = durationFormatter.format(
                    duration = abstinenceRanges.maxDuration().orZero(),
                    accuracy = DurationFormatter.Accuracy.HOURS
                )
            ),
            StatisticData(
                name = strings.statisticsMinAbstinenceTime(),
                value = durationFormatter.format(
                    duration = abstinenceRanges.minDuration().orZero(),
                    accuracy = DurationFormatter.Accuracy.HOURS
                )
            ),
            StatisticData(
                name = strings.statisticsDurationSinceFirstTrack(),
                value = durationFormatter.format(
                    duration = habitAbstinenceDurationSinceFirstTrack(
                        failedRanges = failedRanges,
                        currentTime = currentTime
                    ).orZero(),
                    accuracy = DurationFormatter.Accuracy.HOURS
                )
            ),
            StatisticData(
                name = strings.statisticsCountEventsInCurrentMonth(),
                value = numberFormatter.format(
                    habitEventRecords.countEventsInMonth(
                        monthOfYear = currentTime.monthOfYear(timeZone),
                        timeZone = timeZone
                    )
                )
            ),
            StatisticData(
                name = strings.statisticsCountEventsInPreviousMonth(),
                value = numberFormatter.format(
                    habitEventRecords.countEventsInMonth(
                        monthOfYear = currentTime.monthOfYear(timeZone).previous(),
                        timeZone = timeZone
                    )
                )
            ),
            StatisticData(
                name = strings.statisticsTotalCountEvents(),
                value = numberFormatter.format(
                    habitEventRecords.countEvents()
                )
            )
        )
    } else {
        emptyList()
    }
}