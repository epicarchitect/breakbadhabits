package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HabitDeletionFeature(
    private val coroutineScope: CoroutineScope,
    private val habitsRepository: HabitsRepository,
    private val habitId: Int
) {

    private val mutableState = MutableStateFlow<State>(State.NotExecuted())
    val state = mutableState.asStateFlow()

    fun startDeletion() {
        coroutineScope.launch {
            mutableState.value = State.Executing()
            habitsRepository.deleteHabit(habitId)
            mutableState.value = State.Executed()
        }
    }

    sealed class State {
        class NotExecuted : State()
        class Executing : State()
        class Executed : State()
    }
}