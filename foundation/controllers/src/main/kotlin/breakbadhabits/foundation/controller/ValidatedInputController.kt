package breakbadhabits.foundation.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ValidatedInputController<INPUT, VALIDATION_RESULT>(
    private val coroutineScope: CoroutineScope,
    initialInput: INPUT,
    private val validation: suspend (INPUT) -> VALIDATION_RESULT?
) {
    private val inputState = MutableStateFlow(initialInput)
    private val validationResultState = MutableStateFlow<VALIDATION_RESULT?>(null)

    val state = combine(
        inputState,
        validationResultState
    ) { input, result ->
        State(input, result)
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State(initialInput, null)
    )

    suspend fun validateAndAwait() = validation(inputState.value).also {
        validationResultState.value = it
    }

    fun validate() {
        coroutineScope.launch {
            validateAndAwait()
        }
    }

    fun changeInput(value: INPUT) {
        inputState.value = value
        validate()
    }

    class State<INPUT, VALIDATION_RESULT>(
        val input: INPUT,
        val validationResult: VALIDATION_RESULT
    )
}