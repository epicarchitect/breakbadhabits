package breakbadhabits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.logic.CurrentHabitAbstinenceProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CurrentHabitAbstinenceViewModel(
    currentHabitAbstinenceProvider: CurrentHabitAbstinenceProvider,
    habitAbstinenceFormatter: HabitAbstinenceFormatter,
    habitId: Habit.Id
) : EpicViewModel() {

    val state = currentHabitAbstinenceProvider.provideFlow(habitId).map {
        if (it == null) State.NotExist()
        else State.Loaded(habitAbstinenceFormatter.format(it))
    }.stateIn(coroutineScope, SharingStarted.Eagerly, State.Loading())

    sealed class State {
        class Loading : State()
        class NotExist : State()
        class Loaded(val formattedAbstinence: FormattedHabitAbstinence) : State()
    }
}