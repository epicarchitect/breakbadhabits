package breakbadhabits.logic

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitAbstinence
import breakbadhabits.extension.datetime.LocalDateTimeInterval
import breakbadhabits.logic.dependecy.repository.HabitTracksRepository
import breakbadhabits.logic.dependecy.time.TimeProvider
import kotlinx.coroutines.flow.combine

class CurrentHabitAbstinenceProvider(
    private val habitTracksRepository: HabitTracksRepository,
    private val timeProvider: TimeProvider
) {
    fun provideFlow(habitId: Habit.Id) = combine(
        habitTracksRepository.habitTrackFlowByHabitIdAndLastByTime(habitId),
        timeProvider.currentTimeFlow()
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