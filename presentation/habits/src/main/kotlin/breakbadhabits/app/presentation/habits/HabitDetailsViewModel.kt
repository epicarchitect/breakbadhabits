package breakbadhabits.app.presentation.habits

import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.habits.provider.HabitProvider
import breakbadhabits.foundation.controller.LoadingController
import breakbadhabits.foundation.viewmodel.ViewModel

class HabitDetailsViewModel(
    habitProvider: HabitProvider,
    habitId: Habit.Id
) : ViewModel() {

    val habitController = LoadingController(
        coroutineScope = viewModelScope,
        flow = habitProvider.provideHabitFlowById(habitId)
    )
}