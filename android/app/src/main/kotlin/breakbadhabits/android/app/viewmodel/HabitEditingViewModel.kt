package breakbadhabits.android.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.validator.HabitValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HabitEditingViewModel(
    private val habitsRepository: HabitsRepository,
    private val habitValidator: HabitValidator,
    private val habitId: Int,
) : ViewModel() {

    val initialHabit = habitsRepository.habitByIdFlow(habitId)

    private val _habitNameStateFlow = MutableStateFlow<String?>(null)
    val habitNameStateFlow = _habitNameStateFlow.asStateFlow()

    private val _habitIconIdStateFlow = MutableStateFlow(0)
    val habitIconIdStateFlow = _habitIconIdStateFlow.asStateFlow()

    private val _habitNameValidationStateFlow = MutableStateFlow<HabitNameValidationState>(HabitNameValidationState.NotExecuted())
    val habitNameValidationStateFlow = _habitNameValidationStateFlow.asStateFlow()

    private val _habitUpdatingStateFlow = MutableStateFlow<HabitUpdatingState>(HabitUpdatingState.NotExecuted())
    val habitUpdatingStateFlow = _habitUpdatingStateFlow.asStateFlow()

    private val _habitUpdatingAllowedStateFlow = MutableStateFlow(false)
    val habitUpdatingAllowedStateFlow = _habitUpdatingAllowedStateFlow.asStateFlow()

    init {
        initialHabit.onEach {
            val habit = it ?: return@onEach
            _habitIconIdStateFlow.value = habit.iconId
            _habitNameStateFlow.value = habit.name
        }.launchIn(viewModelScope)

        _habitNameValidationStateFlow.onEach {
            _habitUpdatingAllowedStateFlow.value = isHabitUpdatingAllowed()
        }.launchIn(viewModelScope)

        _habitIconIdStateFlow.onEach {
            _habitUpdatingAllowedStateFlow.value = isHabitUpdatingAllowed()
        }.launchIn(viewModelScope)

        habitNameStateFlow.onEach {
            when (it) {
                null -> {
                    _habitNameValidationStateFlow.value = HabitNameValidationState.NotExecuted()
                }
                else -> {
                    _habitNameValidationStateFlow.value = HabitNameValidationState.Executing()
                    _habitNameValidationStateFlow.value = HabitNameValidationState.Executed(validateHabitName(it))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun updateHabitIconId(habitIconId: Int) {
        _habitIconIdStateFlow.value = habitIconId
    }

    fun updateHabitName(habitName: String?) {
        _habitNameStateFlow.value = habitName
    }

    fun saveHabit() = viewModelScope.launch {
        require(_habitUpdatingAllowedStateFlow.value) { "Habit updating must be allowed!" }

        _habitUpdatingStateFlow.value = HabitUpdatingState.Executing()
        habitsRepository.updateHabit(
            habitId,
            _habitNameStateFlow.value!!,
            _habitIconIdStateFlow.value
        )
        _habitUpdatingStateFlow.value = HabitUpdatingState.Executed()
    }

    private suspend fun validateHabitName(habitName: String) = when {
        habitName == initialHabit.first()?.name -> HabitNameValidationResult.Valid(habitName)
        !habitValidator.nameNotEmpty(habitName) -> HabitNameValidationResult.Empty(habitName)
        !habitValidator.nameNotLongerThenMaxNameLength(habitName) -> HabitNameValidationResult.TooLong(
            habitName,
            habitValidator.maxHabitNameLength
        )
        !habitValidator.nameNotUsed(habitName) -> HabitNameValidationResult.Used(habitName)
        else -> HabitNameValidationResult.Valid(habitName)
    }

    private suspend fun isHabitUpdatingAllowed() =
        habitNameValidationStateFlow.value.let {
            it is HabitNameValidationState.Executed && it.result is HabitNameValidationResult.Valid
        } && initialHabit.first()?.let {
            _habitIconIdStateFlow.value != it.iconId || _habitNameStateFlow.value != it.name
        } ?: false

    sealed class HabitUpdatingState {
        class NotExecuted : HabitUpdatingState()
        class Executing : HabitUpdatingState()
        class Executed : HabitUpdatingState()
    }

    sealed class HabitNameValidationState {
        class NotExecuted : HabitNameValidationState()
        class Executing : HabitNameValidationState()
        class Executed(val result: HabitNameValidationResult) : HabitNameValidationState()
    }

    sealed class HabitNameValidationResult(val value: String) {
        class Valid(value: String) : HabitNameValidationResult(value)
        class TooLong(value: String, val maxHabitNameLength: Int) : HabitNameValidationResult(value)
        class Used(value: String) : HabitNameValidationResult(value)
        class Empty(value: String) : HabitNameValidationResult(value)
    }
}