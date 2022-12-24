package breakbadhabits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.logic.HabitDeleterModule

class HabitDeletionModule(
    private val habitDeleterModule: HabitDeleterModule
) {

    fun createHabitIdsViewModel(habitId: Habit.Id) = HabitDeletionViewModel(
        habitDeleter = habitDeleterModule.createHabitIdsProvider(),
        habitId = habitId
    )

}