package breakbadhabits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.logic.HabitTrackCreatorModule

class HabitTrackCreationModule(
    private val habitTrackCreatorModule: HabitTrackCreatorModule
) {

    fun createHabitTrackCreationViewModel(habitId: Habit.Id) = HabitTrackCreationViewModel(
        habitTrackCreator = habitTrackCreatorModule.createHabitTrackCreator(),
        habitId = habitId
    )

}