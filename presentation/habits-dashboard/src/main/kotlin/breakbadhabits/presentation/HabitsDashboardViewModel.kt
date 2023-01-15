package breakbadhabits.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitAbstinence
import breakbadhabits.logic.HabitAbstinenceProvider
import breakbadhabits.logic.HabitProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HabitsDashboardViewModel(
    habitProvider: HabitProvider,
    abstinenceProvider: HabitAbstinenceProvider
) : ViewModel() {

//    @OptIn(ExperimentalCoroutinesApi::class)
//    val state = habitProvider.provideHabitsFlow().flatMapLatest { habits ->
//        if (habits.isEmpty()) flowOf(State.NotExist())
//        else combine(
//            habits.map { habit ->
//                abstinenceProvider.provideCurrentHabitAbstinenceFlow(habit.id).map { abstinence ->
//                    HabitItem(habit, abstinence)
//                }
//            }
//        ) {
//            State.Loaded(it.toList())
//        }
//    }.stateIn(viewModelScope, SharingStarted.Eagerly, State.Loading())

    val state = habitProvider.provideHabitsFlow().map { habits ->
        if (habits.isEmpty()) State.NotExist()
        else State.Loaded(
            habits.map { habit ->
                HabitItem(
                    habit,
                    abstinenceProvider.provideCurrentHabitAbstinenceFlow(habit.id)
                )
            }
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, State.Loading())

    sealed class State {
        class Loading : State()
        class NotExist : State()
        class Loaded(val items: List<HabitItem>) : State()
    }

    data class HabitItem(
        val habit: Habit,
        val abstinence: Flow<HabitAbstinence?>
    )
}