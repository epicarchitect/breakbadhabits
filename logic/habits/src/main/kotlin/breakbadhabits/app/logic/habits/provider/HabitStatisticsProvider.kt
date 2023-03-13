package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitStatistics
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.foundation.datetime.MonthOfYear
import breakbadhabits.foundation.datetime.countDays
import breakbadhabits.foundation.datetime.countDaysInMonth
import breakbadhabits.foundation.datetime.monthOfYear
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class HabitStatisticsProvider(
    private val habitTrackProvider: HabitTrackProvider,
    private val habitAbstinenceProvider: HabitAbstinenceProvider,
    private val dateTimeProvider: DateTimeProvider
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
    }

    private fun habitAbstinenceFlow(
        habitId: Habit.Id
    ) = combine(
        habitAbstinenceProvider.abstinenceListFlow(habitId),
        habitTrackProvider.habitTracksFlow(habitId),
        dateTimeProvider.currentTimeFlow()
    ) { abstinenceList, tracks, currentTime ->
        if (tracks.isEmpty()) return@combine null

        val timesInSeconds = abstinenceList.map {
            it.range.value.endInclusive.epochSeconds - it.range.value.start.epochSeconds
        }

        HabitStatistics.Abstinence(
            averageTime = timesInSeconds.average().toDuration(DurationUnit.SECONDS),
            maxTime = timesInSeconds.max().toDuration(DurationUnit.SECONDS),
            minTime = timesInSeconds.min().toDuration(DurationUnit.SECONDS),
            timeSinceFirstTrack = currentTime - tracks.minOf { it.range.value.start }
        )
    }

    private fun habitEventCountFlow(
        habitId: Habit.Id
    ) = habitTrackProvider.habitTracksFlow(habitId).map { tracks ->
        val timeZone = TimeZone.currentSystemDefault()
        val currentDate = Clock.System.now().toLocalDateTime(timeZone).date
        val previousMonthDate = currentDate.minus(DateTimeUnit.MONTH)

        HabitStatistics.EventCount(
            currentMonthCount = tracks.countEventsInMonth(
                monthOfYear = currentDate.monthOfYear,
                timeZone = timeZone
            ),
            previousMonthCount = tracks.countEventsInMonth(
                monthOfYear = previousMonthDate.monthOfYear,
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
    total + track.range.value.countDaysInMonth(monthOfYear, timeZone) * track.eventCount.dailyCount
}

private fun List<HabitTrack>.countEvents(
    timeZone: TimeZone
) = fold(0) { total, track ->
    total + track.range.value.countDays(timeZone) * track.eventCount.dailyCount
}

private fun List<HabitTrack>.filterByMonth(
    monthOfYear: MonthOfYear,
    timeZone: TimeZone
) = filter { track ->
    track.range.value.endInclusive.toLocalDateTime(timeZone).let {
        it.month == monthOfYear.month && it.year == monthOfYear.year
    } || track.range.value.start.toLocalDateTime(timeZone).let {
        it.month == monthOfYear.month && it.year == monthOfYear.year
    }
}