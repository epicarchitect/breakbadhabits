package breakbadhabits.android.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.coroutines.flow.mapItems
import breakbadhabits.android.app.data.HabitEventData
import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class HabitsViewModel(private val habitsRepository: HabitsRepository) : ViewModel() {

    val habitsState = habitsRepository.habitListFlow().mapItems { habit ->
        Habit(
            habit.id,
            habit.name,
            habit.iconId,
            habitsRepository.lastByTimeHabitEventByHabitIdFlow(habit.id)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    data class Habit(
        val habitId: Int,
        val habitName: String,
        val habitIconId: Int,
        val lastHabitEvent: Flow<HabitEventData?>
    )
}