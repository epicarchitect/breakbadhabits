package breakbadhabits.logic

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitAbstinence
import kotlinx.coroutines.flow.combine

class CurrentHabitAbstinenceProvider internal constructor(
    private val delegate: CurrentHabitAbstinenceProviderModule.Delegate
) {

    fun provideFlow(habitId: Habit.Id) = combine(
        delegate.habitTrackFlowByHabitIdAndLastByTime(habitId),
        delegate.currentTimeFlow()
    ) { lastTrack, currentDateTime ->
        lastTrack ?: return@combine null
        HabitAbstinence(
            habitId = habitId,
            range = HabitAbstinence.Range(
                lastTrack.range.value.endInclusive..currentDateTime
            )
        )
    }
}