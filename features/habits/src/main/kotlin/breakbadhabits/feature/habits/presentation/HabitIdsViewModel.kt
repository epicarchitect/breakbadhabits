package breakbadhabits.feature.habits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.feature.habits.model.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitIdsViewModel internal constructor(
    private val coroutineScope: CoroutineScope,
    habitsRepository: HabitsRepository
) {

    val state = habitsRepository.habitIdsFlow().map {
        if (it.isEmpty()) State.NotExist()
        else State.Loaded(it)
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), State.Loading())

    fun dispose() {
        coroutineScope.cancel()
    }

    sealed class State {
        class Loading : State()
        class NotExist : State()
        class Loaded(val habitIds: List<Habit.Id>) : State()
    }
}