package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitStatistics
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.foundation.datetime.toMillis
import kotlinx.coroutines.flow.combine

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
        HabitStatistics(
            habitId = habitId,
            abstinence = abstinenceList.let { list ->
                val times = list.map {
                    it.range.value.endInclusive.toMillis() - it.range.value.start.toMillis()
                }
                if (times.isEmpty()) null
                else HabitStatistics.Abstinence(
                    averageTime = times.average().toLong(),
                    maxTime = times.max(),
                    minTime = times.min(),
                    timeSinceFirstTrack = currentTime.toMillis() - list.minOf {
                        it.range.value.start.toMillis()
                    }
                )
            },
            eventCount = HabitStatistics.EventCount( // TODO
                currentMonthCount = 0,
                previousMonthCount = 0,
                totalCount = 0
            )
        )
    }
}