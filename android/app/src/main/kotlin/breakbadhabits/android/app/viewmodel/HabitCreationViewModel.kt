package breakbadhabits.android.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.validator.HabitEventValidator
import breakbadhabits.android.app.validator.HabitValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HabitCreationViewModel(
    private val habitsRepository: HabitsRepository,
    private val habitValidator: HabitValidator,
    private val habitEventValidator: HabitEventValidator
) : ViewModel() {

    private val habitNameStateFlow = MutableStateFlow<String?>(null)
    private val habitIconIdStateFlow = MutableStateFlow(0)
    private val lastEventTimeStateFlow = MutableStateFlow<Long?>(null)
    private val habitCreationStateFlow = MutableStateFlow<HabitCreationState>(HabitCreationState.NotExecuted)
    private val habitNameValidationStateFlow = MutableStateFlow<HabitNameValidationState>(HabitNameValidationState.NotExecuted())
    private val lastHabitEventTimeValidationStateFlow = MutableStateFlow<LastEventTimeValidationState>(LastEventTimeValidationState.NotExecuted())
    private val creationAllowedStateFlow = MutableStateFlow(false)

    init {
        habitNameValidationStateFlow.onEach {
            creationAllowedStateFlow.value = habitCreationAllowed()
        }.launchIn(viewModelScope)

        lastHabitEventTimeValidationStateFlow.onEach {
            creationAllowedStateFlow.value = habitCreationAllowed()
        }.launchIn(viewModelScope)

        habitCreationStateFlow.onEach {
            creationAllowedStateFlow.value = habitCreationAllowed()
        }.launchIn(viewModelScope)

        habitNameStateFlow.onEach {
            when (it) {
                null -> {
                    habitNameValidationStateFlow.value = HabitNameValidationState.NotExecuted()
                }
                else -> {
                    habitNameValidationStateFlow.value = HabitNameValidationState.Executing()
                    habitNameValidationStateFlow.value = HabitNameValidationState.Executed(validateHabitName(it))
                }
            }
        }.launchIn(viewModelScope)

        lastEventTimeStateFlow.onEach {
            when (it) {
                null -> {
                    lastHabitEventTimeValidationStateFlow.value = LastEventTimeValidationState.NotExecuted()
                }
                else -> {
                    lastHabitEventTimeValidationStateFlow.value = LastEventTimeValidationState.Executing()
                    lastHabitEventTimeValidationStateFlow.value = LastEventTimeValidationState.Executed(validateLastEventTime(it))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun habitNameStateFlow() = habitNameStateFlow.asStateFlow()

    fun lastHabitEventTimeStateFlow() = lastEventTimeStateFlow.asStateFlow()

    fun habitIconIdStateFlow() = habitIconIdStateFlow.asStateFlow()

    fun habitCreationStateFlow() = habitCreationStateFlow.asStateFlow()

    fun habitNameValidationStateFlow() = habitNameValidationStateFlow.asStateFlow()

    fun lastHabitEventTimeValidationStateFlow() = lastHabitEventTimeValidationStateFlow.asStateFlow()

    fun creationAllowedStateFlow() = creationAllowedStateFlow.asStateFlow()

    fun changeLastEventTimeStateFlow(timeInMillis: Long) {
        lastEventTimeStateFlow.value = timeInMillis
    }

    fun changeHabitIconIdStateFlow(habitIconId: Int) {
        habitIconIdStateFlow.value = habitIconId
    }

    fun changeHabitNameStateFlow(habitName: String?) {
        habitNameStateFlow.value = habitName
    }

    fun startHabitCreation() = viewModelScope.launch(Dispatchers.IO) {
        require(creationAllowedStateFlow.value) { "Habit creation must be allowed!" }

        habitCreationStateFlow.value = HabitCreationState.Executing
        habitsRepository.createHabit(
            habitNameStateFlow.value!!,
            habitIconIdStateFlow.value,
            lastEventTimeStateFlow.value!!
        )
        habitCreationStateFlow.value = HabitCreationState.Executed
    }

    private fun habitCreationAllowed() = habitNameValidationStateFlow.value.let {
        it is HabitNameValidationState.Executed && it.result is HabitNameValidationResult.Valid
    } && lastHabitEventTimeValidationStateFlow.value.let {
        it is LastEventTimeValidationState.Executed && it.result is LastEventTimeValidationResult.Valid
    } && habitCreationStateFlow.value is HabitCreationState.NotExecuted

    private suspend fun validateHabitName(habitName: String) = when {
        !habitValidator.nameNotEmpty(habitName) -> HabitNameValidationResult.Empty(habitName)
        !habitValidator.nameNotLongerThenMaxNameLength(habitName) -> HabitNameValidationResult.TooLong(
            habitName,
            habitValidator.maxHabitNameLength
        )
        !habitValidator.nameNotUsed(habitName) -> HabitNameValidationResult.Used(habitName)
        else -> HabitNameValidationResult.Valid(habitName)
    }

    private fun validateLastEventTime(time: Long) = when {
        !habitEventValidator.timeNotBiggestThenCurrentTime(time) -> LastEventTimeValidationResult.BiggestThenCurrentTime(time)
        else -> LastEventTimeValidationResult.Valid(time)
    }

    sealed class HabitCreationState {
        object NotExecuted : HabitCreationState()
        object Executing : HabitCreationState()
        object Executed : HabitCreationState()
    }

    sealed class LastEventTimeValidationState {
        class NotExecuted : LastEventTimeValidationState()
        class Executing : LastEventTimeValidationState()
        class Executed(val result: LastEventTimeValidationResult) : LastEventTimeValidationState()
    }

    sealed class HabitNameValidationState {
        class NotExecuted : HabitNameValidationState()
        class Executing : HabitNameValidationState()
        class Executed(val result: HabitNameValidationResult) : HabitNameValidationState()
    }

    sealed class LastEventTimeValidationResult(val value: Long) {
        class Valid(value: Long) : LastEventTimeValidationResult(value)
        class BiggestThenCurrentTime(value: Long) : LastEventTimeValidationResult(value)
    }

    sealed class HabitNameValidationResult(val value: String) {
        class Valid(value: String) : HabitNameValidationResult(value)
        class TooLong(value: String, val maxHabitNameLength: Int) : HabitNameValidationResult(value)
        class Used(value: String) : HabitNameValidationResult(value)
        class Empty(value: String) : HabitNameValidationResult(value)
    }
}