package breakbadhabits.app.presentation.habits

import breakbadhabits.framework.viewmodel.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.habits.deleter.HabitDeleter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HabitDeletionViewModel(
    private val habitDeleter: HabitDeleter,
    private val habitId: Habit.Id
) : ViewModel() {

    private val mutableState = MutableStateFlow<State>(State.NotStarted())
    val state = mutableState.asStateFlow()

    fun startDeletion() {
        mutableState.value = State.Confirming()
    }

    fun confirm() {
        mutableState.value = State.Executing()
        viewModelScope.launch {
            habitDeleter.deleteById(habitId)
            mutableState.value = State.Executed()
        }
    }

    fun cancelConfirming() {
        mutableState.value = State.NotStarted()
    }

    sealed class State {
        class NotStarted : State()
        class Confirming : State()
        class Executing : State()
        class Executed : State()
    }
}