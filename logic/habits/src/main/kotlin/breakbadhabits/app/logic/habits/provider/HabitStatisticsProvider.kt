package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitStatistics
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.foundation.datetime.countDays
import breakbadhabits.foundation.datetime.countDaysInMonth
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import java.time.YearMonth
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class HabitStatisticsProvider(
    private val habitTrackProvider: HabitTrackProvider,
    private val habitAbstinenceProvider: HabitAbstinenceProvider,
    private val dateTimeProvider: DateTimeProvider
) {
    fun habitStatisticsFlowById(
        habitId: Habit.Id
    ) = combine(
        habitAbstinenceProvider.provideAbstinenceListById(habitId),
        habitTrackProvider.provideByHabitId(habitId),
        dateTimeProvider.currentTimeFlow()
    ) { abstinenceList, tracks, currentTime ->
        val timeZone = TimeZone.currentSystemDefault()
        val currentDate = Clock.System.now().toLocalDateTime(timeZone).date
        val previousMonthDate = currentDate.minus(DateTimeUnit.MONTH)

        HabitStatistics(
            habitId = habitId,
            abstinence = abstinenceList.let { list ->
                if (list.isEmpty()) return@let null

                val timesInSeconds = list.map {
                    it.range.value.endInclusive.epochSeconds - it.range.value.start.epochSeconds
                }

                HabitStatistics.Abstinence(
                    averageTime = timesInSeconds.average().toDuration(DurationUnit.SECONDS),
                    maxTime = timesInSeconds.max().toDuration(DurationUnit.SECONDS),
                    minTime = timesInSeconds.min().toDuration(DurationUnit.SECONDS),
                    timeSinceFirstTrack = currentTime - list.minOf { it.range.value.start }
                )
            },
            eventCount = HabitStatistics.EventCount(
                currentMonthCount = tracks.countEventsInMonth(
                    YearMonth.of(currentDate.year, currentDate.month),
                    timeZone
                ),
                previousMonthCount = tracks.countEventsInMonth(
                    YearMonth.of(previousMonthDate.year, previousMonthDate.month),
                    timeZone
                ),
                totalCount = tracks.countEvents(timeZone)
            )
        )
    }
}

private fun List<HabitTrack>.countEventsInMonth(
    yearMonth: YearMonth,
    timeZone: TimeZone
) = filterByMonth(yearMonth.month).fold(0) { total, track ->
    total + track.range.value.countDaysInMonth(yearMonth, timeZone) * track.eventCount.dailyCount
}

private fun List<HabitTrack>.countEvents(
    timeZone: TimeZone
) = fold(0) { total, track ->
    total + track.range.value.countDays(timeZone) * track.eventCount.dailyCount
}

private fun List<HabitTrack>.filterByMonth(month: Month) = filter {
    it.range.value.endInclusive.toLocalDateTime(
        timeZone = TimeZone.currentSystemDefault()
    ).month == month || it.range.value.start.toLocalDateTime(
        timeZone = TimeZone.currentSystemDefault()
    ).month == month
}