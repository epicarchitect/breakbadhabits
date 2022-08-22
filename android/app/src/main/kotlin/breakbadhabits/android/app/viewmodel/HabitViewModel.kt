package breakbadhabits.android.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.data.HabitEvent
import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HabitViewModel(
    habitsRepository: HabitsRepository,
    habitId: Int,
) : ViewModel() {

    val habitFlow = MutableStateFlow<HabitState>(HabitState.Loading())

    init {
        habitsRepository.habitByIdFlow(habitId).onEach {
            habitFlow.value = HabitState.Loaded(
                it?.let { habit ->
                    Habit(
                        habit.name,
                        habit.iconId,
                        habitsRepository.lastByTimeHabitEventByHabitIdFlow(habit.id)
                    )
                }
            )
        }.launchIn(viewModelScope)
    }

    data class Habit(
        val name: String,
        val iconId: Int,
        val lastHabitEvent: Flow<HabitEvent?>
    )

    sealed class HabitState {
        class Loading : HabitState()
        class Loaded(val habit: Habit?) : HabitState()
    }
}