package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HabitUpdatingFeature(
    private val coroutineScope: CoroutineScope,
    private val habitsRepository: HabitsRepository,
    private val habitId: Int
) {

    private val mutableState = MutableStateFlow<State>(State.NotExecuted)
    val state = mutableState.asStateFlow()

    fun startUpdating(
        habitName: String,
        habitIconId: Int
    ) {
        coroutineScope.launch {
            mutableState.value = State.Executing
            habitsRepository.updateHabit(
                habitId,
                habitName,
                habitIconId
            )
            mutableState.value = State.Executed
        }
    }

    sealed class State {
        object NotExecuted : State()
        object Executing : State()
        object Executed : State()
    }
}