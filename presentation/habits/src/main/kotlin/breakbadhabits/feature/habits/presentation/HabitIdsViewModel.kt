package breakbadhabits.feature.habits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.feature.EpicViewModel
import breakbadhabits.feature.habits.model.HabitsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitIdsViewModel internal constructor(
    habitsRepository: HabitsRepository
) : EpicViewModel() {

    val state = habitsRepository.habitIdsFlow().map {
        if (it.isEmpty()) State.NotExist()
        else State.Loaded(it)
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), State.Loading())

    sealed class State {
        class Loading : State()
        class NotExist : State()
        class Loaded(val habitIds: List<Habit.Id>) : State()
    }
}