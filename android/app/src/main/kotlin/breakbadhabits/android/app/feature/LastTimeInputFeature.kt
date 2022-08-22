package breakbadhabits.android.app.feature

import breakbadhabits.android.app.validator.HabitEventValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LastTimeInputFeature(
    coroutineScope: CoroutineScope,
    private val habitEventValidator: HabitEventValidator
) {

    private val mutableInput = MutableStateFlow<Long?>(null)
    val input = mutableInput.asStateFlow()

    private val mutableValidation = MutableStateFlow<ValidationState>(ValidationState.NotExecuted())
    val validation = mutableValidation.asStateFlow()

    init {
        mutableInput.onEach {
            when (it) {
                null -> {
                    mutableValidation.value = ValidationState.NotExecuted()
                }
                else -> {
                    mutableValidation.value = ValidationState.Executing()
                    mutableValidation.value = ValidationState.Executed(validateLastEventTime(it))
                }
            }
        }.launchIn(coroutineScope)
    }

    fun changeInput(value: Long?) {
        mutableInput.value = value
    }

    private fun validateLastEventTime(time: Long) = when {
        !habitEventValidator.timeNotBiggestThenCurrentTime(time) -> {
            ValidationResult.BiggestThenCurrentTime(time)
        }
        else -> ValidationResult.Valid(time)
    }

    sealed class ValidationState {
        class NotExecuted : ValidationState()
        class Executing : ValidationState()
        class Executed(val result: ValidationResult) : ValidationState()
    }

    sealed class ValidationResult(val value: Long) {
        class Valid(value: Long) : ValidationResult(value)
        class BiggestThenCurrentTime(value: Long) : ValidationResult(value)
    }
}