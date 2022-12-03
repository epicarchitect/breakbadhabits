package breakbadhabits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.logic.HabitProviderModule

class HabitModule(
    private val habitProviderModule: HabitProviderModule
) {

    fun createHabitViewModel(id: Habit.Id) = HabitViewModel(
        habitProvider = habitProviderModule.createHabitProvider(),
        habitId = id
    )
}