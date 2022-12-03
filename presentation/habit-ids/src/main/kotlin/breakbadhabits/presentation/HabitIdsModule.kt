package breakbadhabits.presentation

import breakbadhabits.logic.HabitIdsProviderModule

class HabitIdsModule(
    private val habitIdsModule: HabitIdsProviderModule
) {

    fun createHabitIdsViewModel() = HabitIdsViewModel(
        habitIdsProvider = habitIdsModule.createHabitIdsProvider()
    )
}