package breakbadhabits.feature.habits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.feature.habits.model.HabitsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitViewModel internal constructor(
    habitsRepository: HabitsRepository,
    habitId: Habit.Id
) : EpicViewModel() {

    val state = habitsRepository.habitFlowById(habitId).map {
        if (it == null) State.NotExist()
        else State.Loaded(it)
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), State.Loading())

    sealed class State {
        class Loading : State()
        class Loaded(val habit: Habit) : State()
        class NotExist : State()
    }
}