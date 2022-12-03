package breakbadhabits.presentation

import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitAbstinence
import breakbadhabits.logic.CurrentHabitAbstinenceProviderModule
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class CurrentHabitAbstinenceViewModel internal constructor(
    providerModule: CurrentHabitAbstinenceProviderModule,
    habitId: Habit.Id
) : EpicViewModel() {

    private val provider = providerModule.createCurrentHabitAbstinenceProvider()

    val state = provider.provideFlow(habitId).map {
        if (it == null) State.NotExist()
        else State.Loaded(it)
    }.stateIn(coroutineScope, SharingStarted.Eagerly, State.Loading())

    sealed class State {
        class Loading : State()
        class NotExist : State()
        class Loaded(val abstinence: HabitAbstinence) : State()
    }
}