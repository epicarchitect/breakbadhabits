package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HabitCreationFeature(
    private val coroutineScope: CoroutineScope,
    private val habitsRepository: HabitsRepository,
) {

    private val mutableState = MutableStateFlow<State>(State.NotExecuted)
    val state = mutableState.asStateFlow()

    fun startCreation(
        habitName: String,
        habitIconId: Int,
        lastEventTime: Long
    ) {
        coroutineScope.launch {
            mutableState.value = State.Executing
            habitsRepository.createHabit(
                habitName, habitIconId, lastEventTime
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