package breakbadhabits.android.app.feature

import breakbadhabits.android.app.repository.HabitsRepository
import breakbadhabits.android.app.validator.HabitEventValidator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HabitEventTimeInputFeature(
    coroutineScope: CoroutineScope,
    private val habitsRepository: HabitsRepository,
    private val habitEventValidator: HabitEventValidator,
    habitEventId: Int?
) {

    var initialInput: Long? = null
        private set

    private val mutableInput = MutableStateFlow<Long?>(null)
    val input = mutableInput.asStateFlow()

    private val mutableValidation = MutableStateFlow<ValidationState>(ValidationState.NotExecuted())
    val validation = mutableValidation.asStateFlow()

    init {
        if (habitEventId != null) {
            coroutineScope.launch {
                habitsRepository.habitEventByIdFlow(habitEventId).first()?.time?.let {
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
                    mutableValidation.value = ValidationState.Executed(validate(it))
                }
            }
        }.launchIn(coroutineScope)
    }

    fun changeInput(value: Long?) {
        mutableInput.value = value
    }

    private fun validate(time: Long) = when {
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