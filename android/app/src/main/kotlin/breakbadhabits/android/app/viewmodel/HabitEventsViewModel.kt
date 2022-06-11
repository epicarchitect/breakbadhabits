package breakbadhabits.android.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.repository.HabitsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HabitEventsViewModel(
    val habitsRepository: HabitsRepository,
    habitId: Int
) : ViewModel() {

    private val _habitEventStateFlow = MutableStateFlow<List<HabitEvent>>(emptyList())
    val habitEventStateFlow = _habitEventStateFlow.asStateFlow()

    init {
        habitsRepository.habitEventListByHabitIdFlow(habitId).onEach {
            _habitEventStateFlow.value = it.map {
                HabitEvent(
                        it.id,
                        it.time,
                        it.comment
                )
            }
        }.launchIn(viewModelScope)
    }

    fun deleteHabitEvent(id: Int) = viewModelScope.launch {
        habitsRepository.deleteHabitEventById(id)
    }

    data class HabitEvent(
            val id: Int,
            val timeInMillis: Long,
            val comment: String?
    )
}