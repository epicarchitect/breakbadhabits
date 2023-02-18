package breakbadhabits.app.presentation.habit.details

import breakbadhabits.framework.viewmodel.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.habit.provider.HabitProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitDetailsViewModel(
    habitProvider: HabitProvider,
    habitId: Habit.Id
) : ViewModel() {

    val state = habitProvider.provideHabitFlowById(habitId).map {
        if (it == null) State.NotExist()
        else State.Loaded(it)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), State.Loading())

    sealed class State {
        class Loading : State()
        class Loaded(val habit: Habit) : State()
        class NotExist : State()
    }
}