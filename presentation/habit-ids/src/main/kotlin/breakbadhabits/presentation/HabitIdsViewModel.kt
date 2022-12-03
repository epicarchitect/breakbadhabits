package breakbadhabits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.logic.HabitIdsProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitIdsViewModel internal constructor(
    habitIdsProvider: HabitIdsProvider
) : EpicViewModel() {

    val state = habitIdsProvider.provideFlow().map {
        if (it.isEmpty()) State.NotExist()
        else State.Loaded(it)
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), State.Loading())

    sealed class State {
        class Loading : State()
        class NotExist : State()
        class Loaded(val habitIds: List<Habit.Id>) : State()
    }
}