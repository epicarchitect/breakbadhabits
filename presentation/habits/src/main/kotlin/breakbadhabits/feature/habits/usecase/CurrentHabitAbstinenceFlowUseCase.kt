package breakbadhabits.feature.habits.usecase

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitAbstinence
import breakbadhabits.extension.datetime.LocalDateTimeInterval
import breakbadhabits.feature.habits.model.HabitTracksRepository
import breakbadhabits.feature.habits.model.TimeProvider
import kotlinx.coroutines.flow.combine

class CurrentHabitAbstinenceFlowUseCase(
    private val habitTracksRepository: HabitTracksRepository,
    private val timeProvider: TimeProvider,
) {
    operator fun invoke(habitId: Habit.Id) = combine(
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