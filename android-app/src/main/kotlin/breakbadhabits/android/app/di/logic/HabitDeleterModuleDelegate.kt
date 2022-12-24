package breakbadhabits.android.app.di.logic

import breakbadhabits.android.app.repository.HabitTracksRepository
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.entity.Habit
import breakbadhabits.logic.HabitDeleterModule

class HabitDeleterModuleDelegate(
    private val habitsRepository: HabitsRepository,
    private val habitTracksRepository: HabitTracksRepository,
) : HabitDeleterModule.Delegate {
    override suspend fun deleteHabitById(id: Habit.Id) {
        habitsRepository.deleteHabit(id)
    }

    override suspend fun deleteHabitTracksByHabitId(id: Habit.Id) {
        habitTracksRepository.deleteHabitTracksByHabitId(id)
    }
}