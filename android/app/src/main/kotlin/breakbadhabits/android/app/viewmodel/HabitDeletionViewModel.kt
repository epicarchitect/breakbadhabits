package breakbadhabits.android.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HabitDeletionViewModel(
    private val habitsRepository: HabitsRepository,
    private val habitId: Int
) : ViewModel() {

    private val _habitDeleteStateFlow = MutableStateFlow<HabitDeleteState>(HabitDeleteState.NotExecuted())
    val habitDeleteStateFlow = _habitDeleteStateFlow.asStateFlow()

    fun deleteHabit() = viewModelScope.launch {
        _habitDeleteStateFlow.value = HabitDeleteState.Executing()
        habitsRepository.deleteHabit(habitId)
        _habitDeleteStateFlow.value = HabitDeleteState.Executed()
    }

    sealed class HabitDeleteState {
        class NotExecuted : HabitDeleteState()
        class Executing : HabitDeleteState()
        class Executed : HabitDeleteState()
    }
}