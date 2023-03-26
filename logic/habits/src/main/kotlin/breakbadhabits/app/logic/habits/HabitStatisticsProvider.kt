package breakbadhabits.app.logic.habits

import breakbadhabits.app.logic.datetime.DateTimeProvider
import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.app.logic.habits.entity.Habit
import breakbadhabits.app.logic.habits.entity.HabitStatistics
import breakbadhabits.app.logic.habits.entity.HabitTrack
import breakbadhabits.app.logic.habits.tracks.HabitTrackProvider
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.foundation.datetime.MonthOfYear
import breakbadhabits.foundation.datetime.averageDuration
import breakbadhabits.foundation.datetime.countDays
import breakbadhabits.foundation.datetime.countDaysInMonth
import breakbadhabits.foundation.datetime.maxDuration
import breakbadhabits.foundation.datetime.minDuration
import breakbadhabits.foundation.datetime.monthOfYear
import breakbadhabits.foundation.datetime.monthOfYearRange
import breakbadhabits.foundation.datetime.previous
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class HabitStatisticsProvider(
    private val habitTrackProvider: HabitTrackProvider,
    private val habitAbstinenceProvider: HabitAbstinenceProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val dateTimeConfigProvider: DateTimeConfigProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    fun statisticsFlow(habitId: Habit.Id) = combine(
        habitAbstinenceFlow(habitId),
        habitEventCountFlow(habitId)
    ) { abstinence, eventCount ->
        if (abstinence == null) null
        else HabitStatistics(
            habitId,
            abstinence,
            eventCount
        )
    }.flowOn(coroutineDispatchers.default)

    private fun habitAbstinenceFlow(
        habitId: Habit.Id
    ) = combine(
        habitAbstinenceProvider.abstinenceListFlow(habitId),
        habitTrackProvider.habitTracksFlow(habitId),
        dateTimeProvider.currentTime
    ) { abstinenceList, tracks, currentTime ->
        if (tracks.isEmpty() || abstinenceList.isEmpty()) return@combine null

        val ranges = abstinenceList.map { it.range }

        HabitStatistics.Abstinence(
            averageTime = ranges.averageDuration(),
            maxTime = ranges.maxDuration(),
            minTime = ranges.minDuration(),
            timeSinceFirstTrack = currentTime - tracks.minOf { it.time.start }
        )
    }

    private fun habitEventCountFlow(
        habitId: Habit.Id
    ) = combine(
        dateTimeConfigProvider.configFlow(),
        habitTrackProvider.habitTracksFlow(habitId)
    ) { dateTimeConfig, tracks ->
        val timeZone = dateTimeConfig.appTimeZone
        val currentDate = Clock.System.now().toLocalDateTime(timeZone).date
        val previousMonthDate = currentDate.minus(DateTimeUnit.MONTH)

        HabitStatistics.EventCount(
            currentMonthCount = tracks.countEventsInMonth(
                monthOfYear = currentDate.monthOfYear,
                timeZone = timeZone
            ),
            previousMonthCount = tracks.countEventsInMonth(
                monthOfYear = currentDate.monthOfYear.previous(),
                timeZone = timeZone
            ),
            totalCount = tracks.countEvents(timeZone)
        )
    }
}

private fun List<HabitTrack>.countEventsInMonth(
    monthOfYear: MonthOfYear,
    timeZone: TimeZone
) = filterByMonth(monthOfYear, timeZone).fold(0) { total, track ->
    total + track.time.countDaysInMonth(monthOfYear, timeZone) * track.eventCount.dailyCount
}

private fun List<HabitTrack>.countEvents(
    timeZone: TimeZone
) = fold(0) { total, track ->
    total + track.time.countDays(timeZone) * track.eventCount.dailyCount
}

private fun List<HabitTrack>.filterByMonth(
    monthOfYear: MonthOfYear,
    timeZone: TimeZone
) = filter { monthOfYear in it.time.monthOfYearRange(timeZone) }