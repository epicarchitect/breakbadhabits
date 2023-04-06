package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.model.HabitStatistics
import breakbadhabits.app.logic.habits.model.HabitTrack
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.foundation.datetime.MonthOfYear
import breakbadhabits.foundation.datetime.averageDuration
import breakbadhabits.foundation.datetime.maxDuration
import breakbadhabits.foundation.datetime.minDuration
import breakbadhabits.foundation.datetime.monthOfYear
import breakbadhabits.foundation.datetime.monthOfYearRange
import breakbadhabits.foundation.datetime.previous
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HabitStatisticsProvider(
    private val habitTrackProvider: HabitTrackProvider,
    private val habitAbstinenceProvider: HabitAbstinenceProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    fun statisticsFlow(habitId: Int) = combine(
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
        habitId: Int
    ) = combine(
        habitAbstinenceProvider.abstinenceListFlow(habitId),
        habitTrackProvider.habitTracksFlow(habitId),
        dateTimeProvider.currentTime
    ) { abstinenceList, tracks, currentTime ->
        if (tracks.isEmpty() || abstinenceList.isEmpty()) return@combine null

        val ranges = abstinenceList.map { it.instantRange }

        HabitStatistics.Abstinence(
            averageDuration = ranges.averageDuration(),
            maxDuration = ranges.maxDuration(),
            minDuration = ranges.minDuration(),
            durationSinceFirstTrack = currentTime - tracks.minOf { it.instantRange.start }
        )
    }

    private fun habitEventCountFlow(
        habitId: Int
    ) = combine(
        dateTimeProvider.currentTime,
        dateTimeProvider.timeZone,
        habitTrackProvider.habitTracksFlow(habitId)
    ) { currentTime, timeZone, tracks ->
        val currentDate = currentTime.toLocalDateTime(timeZone).date

        HabitStatistics.EventCount(
            currentMonthCount = tracks.countEventsInMonth(
                monthOfYear = currentDate.monthOfYear,
                timeZone = timeZone
            ),
            previousMonthCount = tracks.countEventsInMonth(
                monthOfYear = currentDate.monthOfYear.previous(),
                timeZone = timeZone
            ),
            totalCount = tracks.countEvents()
        )
    }
}

private fun List<HabitTrack>.countEventsInMonth(
    monthOfYear: MonthOfYear,
    timeZone: TimeZone
) = filterByMonth(monthOfYear, timeZone).countEvents()

private fun List<HabitTrack>.countEvents() = fold(0) { total, track ->
    total + track.eventCount
}

private fun List<HabitTrack>.filterByMonth(
    monthOfYear: MonthOfYear,
    timeZone: TimeZone
) = filter { monthOfYear in it.instantRange.monthOfYearRange(timeZone) }