package breakbadhabits.android.app.di.logic

import breakbadhabits.android.app.repository.HabitTracksRepository
import breakbadhabits.android.app.utils.TimeProvider
import breakbadhabits.entity.Habit
import breakbadhabits.logic.CurrentHabitAbstinenceProviderModule

class CurrentHabitAbstinenceProviderModuleDelegate(
    private val habitTracksRepository: HabitTracksRepository,
    private val timeProvider: TimeProvider
) : CurrentHabitAbstinenceProviderModule.Delegate {
    override fun habitTrackFlowByHabitIdAndLastByTime(id: Habit.Id) =
        habitTracksRepository.habitTrackFlowByHabitIdAndLastByTime(id)

    override fun currentTimeFlow() = timeProvider.currentTimeFlow()
}