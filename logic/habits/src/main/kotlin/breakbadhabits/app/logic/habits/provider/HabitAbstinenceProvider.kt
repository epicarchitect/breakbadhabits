package breakbadhabits.app.logic.habits.provider

import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAbstinence
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class HabitAbstinenceProvider(
    private val habitTrackProvider: HabitTrackProvider,
    private val dateTimeProvider: DateTimeProvider
) {
    fun provideFlowById(
        habitId: Habit.Id
    ) = habitTrackProvider.provideByHabitIdAndMaxRangeEnd(habitId).flatMapLatest { lastTrack ->
        if (lastTrack == null) flowOf(null)
        else dateTimeProvider.currentTimeFlow().map { currentTime ->
            HabitAbstinence(
                habitId = habitId,
                range = HabitAbstinence.Range(lastTrack.range.value.endInclusive..currentTime)
            )
        }
    }
}