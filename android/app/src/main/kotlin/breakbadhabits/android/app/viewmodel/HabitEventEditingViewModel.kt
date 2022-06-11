package breakbadhabits.android.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.data.HabitData
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.validator.HabitEventValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HabitEventEditingViewModel(
    private val habitsRepository: HabitsRepository,
    private val habitEventValidator: HabitEventValidator,
    private val habitEventId: Int,
) : ViewModel() {

    private val initialHabitEvent = habitsRepository.habitEventByIdFlow(habitEventId)

    private val _eventTimeStateFlow = MutableStateFlow<Long>(0)
    val eventTimeStateFlow = _eventTimeStateFlow.asStateFlow()

    private val _commentStateFlow = MutableStateFlow<String?>(null)
    val commentStateFlow = _commentStateFlow.asStateFlow()

    private val _habitEventUpdatingStateFlow = MutableStateFlow<HabitEventUpdatingState>(HabitEventUpdatingState.NotExecuted())
    val habitEventUpdatingStateFlow = _habitEventUpdatingStateFlow.asStateFlow()

    private val _eventTimeValidationStateFlow = MutableStateFlow<EventTimeValidationState>(EventTimeValidationState.NotExecuted())
    val eventTimeValidationStateFlow = _eventTimeValidationStateFlow.asStateFlow()

    private val _habitEventUpdatingAllowedStateFlow = MutableStateFlow(false)
    val habitEventUpdatingAllowedStateFlow = _habitEventUpdatingAllowedStateFlow.asStateFlow()

    val habitFlow = MutableStateFlow<HabitData?>(null)

    private suspend fun isHabitEventUpdatingAllowed() =
            _eventTimeValidationStateFlow.value.let {
                it is EventTimeValidationState.Executed && it.result is EventTimeValidationResult.Valid
            } && initialHabitEvent.first()?.let {
                it.comment != _commentStateFlow.value || it.time != _eventTimeStateFlow.value
            } ?: false

    init {
        initialHabitEvent.onEach {
            habitFlow.value = it?.habitId?.let { habitsRepository.habitById(it) }
            _commentStateFlow.value = it?.comment
            _eventTimeStateFlow.value = it?.time ?: 0
        }.launchIn(viewModelScope)

        _eventTimeValidationStateFlow.onEach {
            _habitEventUpdatingAllowedStateFlow.value = isHabitEventUpdatingAllowed()
        }.launchIn(viewModelScope)

        _commentStateFlow.onEach {
            _habitEventUpdatingAllowedStateFlow.value = isHabitEventUpdatingAllowed()
        }.launchIn(viewModelScope)

        eventTimeStateFlow.onEach {
            _eventTimeValidationStateFlow.value = EventTimeValidationState.Executing()
            _eventTimeValidationStateFlow.value = EventTimeValidationState.Executed(validateEventTime(it))
        }.launchIn(viewModelScope)
    }

    fun updateEventTime(timeInMillis: Long) {
        _eventTimeStateFlow.value = timeInMillis
    }

    fun updateComment(comment: String?) {
        _commentStateFlow.value = comment?.ifEmpty { null }
    }

    fun startHabitEventUpdating() = viewModelScope.launch {
        require(_habitEventUpdatingAllowedStateFlow.value) { "Habit event creation must be allowed!" }

        _habitEventUpdatingStateFlow.value = HabitEventUpdatingState.Executing()
        habitsRepository.updateHabitEvent(
            habitEventId,
            _eventTimeStateFlow.value,
            _commentStateFlow.value
        )
        _habitEventUpdatingStateFlow.value = HabitEventUpdatingState.Executed()
    }

    private fun validateEventTime(time: Long) = when {
        !habitEventValidator.timeNotBiggestThenCurrentTime(time) -> EventTimeValidationResult.BiggestThenCurrentTime(time)
        else -> EventTimeValidationResult.Valid(time)
    }

    fun deleteEvent() = viewModelScope.launch {
        habitsRepository.deleteHabitEventById(habitEventId)
    }

    sealed class HabitEventUpdatingState {
        class NotExecuted : HabitEventUpdatingState()
        class Executing : HabitEventUpdatingState()
        class Executed : HabitEventUpdatingState()
    }

    sealed class EventTimeValidationState {
        class NotExecuted : EventTimeValidationState()
        class Executing : EventTimeValidationState()
        class Executed(val result: EventTimeValidationResult) : EventTimeValidationState()
    }

    sealed class EventTimeValidationResult(val value: Long) {
        class Valid(value: Long) : EventTimeValidationResult(value)
        class BiggestThenCurrentTime(value: Long) : EventTimeValidationResult(value)
    }
}