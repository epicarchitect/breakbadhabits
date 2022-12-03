package breakbadhabits.android.app.di.logic

import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.entity.Habit
import breakbadhabits.logic.HabitProviderModule

class HabitProviderModuleDelegate(
    private val habitsRepository: HabitsRepository
) : HabitProviderModule.Delegate {
    override fun habitFlow(id: Habit.Id) = habitsRepository.habitFlowById(id)

    override suspend fun getHabit(id: Habit.Id) = habitsRepository.getHabitById(id)
}