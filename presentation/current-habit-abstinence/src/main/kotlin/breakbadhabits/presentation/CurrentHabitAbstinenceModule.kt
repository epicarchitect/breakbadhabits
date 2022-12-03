package breakbadhabits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.logic.CurrentHabitAbstinenceProviderModule

class CurrentHabitAbstinenceModule(
    private val providerModule: CurrentHabitAbstinenceProviderModule
) : EpicViewModel() {

    fun createCurrentHabitAbstinenceViewModel(
        habitId: Habit.Id
    ) = CurrentHabitAbstinenceViewModel(
        providerModule,
        habitId
    )
}