package epicarchitect.breakbadhabits.logic.habits.provider

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.datetime.MonthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.averageDuration
import epicarchitect.breakbadhabits.foundation.datetime.duration
import epicarchitect.breakbadhabits.foundation.datetime.maxDuration
import epicarchitect.breakbadhabits.foundation.datetime.minDuration
import epicarchitect.breakbadhabits.foundation.datetime.monthOfYear
import epicarchitect.breakbadhabits.foundation.datetime.previous
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.datetime.provider.currentDateTimeFlow
import epicarchitect.breakbadhabits.logic.habits.model.HabitStatistics
import epicarchitect.breakbadhabits.logic.habits.model.HabitTrack
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.toInstant

class HabitStatisticsProvider(
    private val habitTrackProvider: HabitTrackProvider,
    private val habitAbstinenceProvider: HabitAbstinenceProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    fun statisticsFlow(habitId: Int) = combine(
        abstinenceFlow(habitId),
        eventAmountFlow(habitId)
    ) { abstinence, eventCount ->
        if (abstinence == null) {
            null
        } else {
            HabitStatistics(
                habitId,
                abstinence,
                eventCount
            )
        }
    }.flowOn(coroutineDispatchers.default)

    private fun abstinenceFlow(
        habitId: Int
    ) = combine(
        habitAbstinenceProvider.abstinenceListFlow(habitId),
        habitTrackProvider.habitTracksFlow(habitId),
        dateTimeProvider.currentDateTimeFlow(),
        dateTimeProvider.currentTimeZoneFlow()
    ) { abstinenceList, tracks, currentTime, timeZone ->
        if (tracks.isEmpty() || abstinenceList.isEmpty()) return@combine null

        val ranges = abstinenceList.map {
            it.dateTimeRange.let {
                it.start.toInstant(timeZone)..it.endInclusive.toInstant(timeZone)
            }
        }

        HabitStatistics.Abstinence(
            averageDuration = ranges.averageDuration(),
            maxDuration = ranges.maxDuration(),
            minDuration = ranges.minDuration(),
            durationSinceFirstTrack = (currentTime..tracks.minOf { it.dateTimeRange.start }).duration(timeZone)
        )
    }

    private fun eventAmountFlow(
        habitId: Int
    ) = combine(
        habitTrackProvider.habitTracksFlow(habitId),
        dateTimeProvider.currentDateTimeFlow()
    ) { tracks, currentDateTime ->
        val currentMonth = currentDateTime.monthOfYear
        HabitStatistics.EventAmount(
            currentMonthCount = tracks.countEventsInMonth(currentMonth),
            previousMonthCount = tracks.countEventsInMonth(currentMonth.previous()),
            totalCount = tracks.countEvents()
        )
    }
}

private fun List<HabitTrack>.countEventsInMonth(
    monthOfYear: MonthOfYear
) = filterByMonth(monthOfYear).countEvents()

private fun List<HabitTrack>.countEvents() = fold(0) { total, track ->
    total + track.eventCount
}

private fun List<HabitTrack>.filterByMonth(
    monthOfYear: MonthOfYear
) = filter {
    monthOfYear in it.dateTimeRange.let {
        it.start.monthOfYear..it.endInclusive.monthOfYear
    }
}