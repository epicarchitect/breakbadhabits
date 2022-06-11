package breakbadhabits.android.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.validator.HabitEventValidator
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HabitEventCreationViewModel(
    private val habitsRepository: HabitsRepository,
    private val habitEventValidator: HabitEventValidator,
    private val habitId: Int
) : ViewModel() {

    val timeStateFlow = MutableStateFlow(System.currentTimeMillis())
    val timeValidationStateFlow = MutableStateFlow<TimeValidationState>(TimeValidationState.NotExecuted())
    val commentStateFlow = MutableStateFlow<String?>(null)
    val creationStateFlow = MutableStateFlow<Deferred<Unit>?>(null)
    val creationAllowedStateFlow = MutableStateFlow(false)

    private fun creationAllowed() = timeValidationStateFlow.value.let {
        it is TimeValidationState.Executed && it.result is TimeValidationResult.Valid
    }

    init {
        timeValidationStateFlow.onEach {
            creationAllowedStateFlow.update {
                creationAllowed()
            }
        }.launchIn(viewModelScope)

        timeStateFlow.onEach { time ->
            timeValidationStateFlow.update {
                TimeValidationState.Executed(validateTime(time))
            }
        }.launchIn(viewModelScope)
    }

    fun updateTime(time: Long) = viewModelScope.launch {
        timeStateFlow.update { time }
    }

    fun updateComment(comment: String?) = viewModelScope.launch {
        commentStateFlow.update {
            comment?.let {
                if (it.trim().isEmpty()) null else it
            }
        }
    }

    fun startCreation() = viewModelScope.launch {
        require(creationAllowedStateFlow.value) { "Habit event creation must be allowed!" }

        creationStateFlow.update {
            viewModelScope.async {
                habitsRepository.createHabitEvent(
                    habitId,
                    timeStateFlow.value,
                    commentStateFlow.value
                )
            }
        }
    }

    private fun validateTime(time: Long) = when {
        !habitEventValidator.timeNotBiggestThenCurrentTime(time) -> TimeValidationResult.BiggestThenCurrentTime(time)
        else -> TimeValidationResult.Valid(time)
    }

    sealed class TimeValidationState {
        class NotExecuted : TimeValidationState()
        class Executing : TimeValidationState()
        class Executed(val result: TimeValidationResult) : TimeValidationState()
    }

    sealed class TimeValidationResult(val value: Long) {
        class Valid(value: Long) : TimeValidationResult(value)
        class BiggestThenCurrentTime(value: Long) : TimeValidationResult(value)
    }
}