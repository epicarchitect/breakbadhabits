package breakbadhabits.logic

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitAbstinence
import breakbadhabits.extension.datetime.LocalDateTimeInterval
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
            interval = HabitAbstinence.Interval(
                LocalDateTimeInterval(
                    start = lastTrack.interval.value.end,
                    end = currentDateTime
                )
            )
        )
    }
}