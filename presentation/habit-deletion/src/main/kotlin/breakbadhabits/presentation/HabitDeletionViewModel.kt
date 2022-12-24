package breakbadhabits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.logic.HabitDeleter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HabitDeletionViewModel internal constructor(
    private val habitDeleter: HabitDeleter,
    private val habitId: Habit.Id
) : EpicViewModel() {

    private val mutableState = MutableStateFlow<State>(State.NotStarted())
    val state = mutableState.asStateFlow()

    fun startDeletion() {
        mutableState.value = State.Confirming()
    }

    fun confirm() {
        mutableState.value = State.Executing()
        coroutineScope.launch {
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