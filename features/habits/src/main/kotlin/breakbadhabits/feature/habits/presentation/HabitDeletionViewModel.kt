package breakbadhabits.feature.habits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.feature.habits.model.HabitTracksRepository
import breakbadhabits.feature.habits.model.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HabitDeletionViewModel internal constructor(
    private val coroutineScope: CoroutineScope,
    private val habitsRepository: HabitsRepository,
    private val habitTracksRepository: HabitTracksRepository,
    private val habitId: Habit.Id
) {

    private val mutableState = MutableStateFlow<State>(State.Ready())
    val state = mutableState.asStateFlow()

    fun startDelete() {
        require(mutableState.value is State.Ready)
        mutableState.value = State.InProcess()

        coroutineScope.launch {
            habitsRepository.deleteHabit(habitId)
            habitTracksRepository.deleteHabitTracksByHabitId(habitId)
            mutableState.value = State.HabitDeleted()
        }
    }

    fun dispose() {
        coroutineScope.cancel()
    }

    sealed class State {
        class Ready : State()
        class InProcess : State()
        class HabitDeleted : State()
    }
}