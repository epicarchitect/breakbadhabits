package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.DefaultHabitsRepository
import breakbadhabits.android.app.validator.HabitValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HabitNameInputFeature(
    coroutineScope: CoroutineScope,
    private val habitsRepository: DefaultHabitsRepository,
    private val habitValidator: HabitValidator,
    private val habitId: Int?
) {
    var initialInput: String? = null
        private set

    private val mutableInput = MutableStateFlow(initialInput)
    val input = mutableInput.asStateFlow()

    private val mutableValidation = MutableStateFlow<ValidationState>(ValidationState.NotExecuted())
    val validation = mutableValidation.asStateFlow()

    init {
        if (habitId != null) {
            coroutineScope.launch {
                habitsRepository.habitByIdFlow(habitId).first()?.name?.let {
                    initialInput = it
                    mutableInput.value = it
                }
            }
        }

        mutableInput.onEach {
            when (it) {
                null -> {
                    mutableValidation.value = ValidationState.NotExecuted()
                }

                else -> {
                    mutableValidation.value = ValidationState.Executing()
                    mutableValidation.value = ValidationState.Executed(validateHabitName(it))
                }
            }
        }.launchIn(coroutineScope)
    }

    fun changeInput(value: String?) {
        mutableInput.value = value
    }

    private suspend fun validateHabitName(habitName: String) = when {
        habitName == initialInput -> ValidationResult.Valid(habitName)
        !habitValidator.nameNotEmpty(habitName) -> ValidationResult.Empty(habitName)
        !habitValidator.nameNotLongerThenMaxNameLength(habitName) -> ValidationResult.TooLong(
            habitName,
            habitValidator.maxHabitNameLength
        )

        !habitValidator.nameNotUsed(habitName) -> ValidationResult.Used(habitName)
        else -> ValidationResult.Valid(habitName)
    }

    sealed class ValidationState {
        class NotExecuted : ValidationState()
        class Executing : ValidationState()
        class Executed(val result: ValidationResult) : ValidationState()
    }

    sealed class ValidationResult(val value: String) {
        class Valid(value: String) : ValidationResult(value)
        class TooLong(value: String, val maxHabitNameLength: Int) : ValidationResult(value)
        class Used(value: String) : ValidationResult(value)
        class Empty(value: String) : ValidationResult(value)
    }
}