package breakbadhabits.android.app.di.logic

import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.logic.HabitIdsProviderModule

class HabitIdsProviderModuleDelegate(
    private val habitsRepository: HabitsRepository
) : HabitIdsProviderModule.Delegate {
    override fun habitIdsFlow() = habitsRepository.habitIdsFlow()
}