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
    private val validation: suspend INPUT.() -> VALIDATION_RESULT?,
    private val decorateInput: (INPUT) -> INPUT = { it }
) : StateController<ValidatedInputController.State<INPUT, out VALIDATION_RESULT?>> {
    private val inputState = MutableStateFlow(decorateInput(initialInput))
    private val validationResultState = MutableStateFlow<VALIDATION_RESULT?>(null)

    override val state = combine(inputState, validationResultState, ::State).stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            input = inputState.value,
            validationResult = validationResultState.value
        )
    )

    suspend fun validateAndAwait() = inputState.value.validation().also {
        validationResultState.value = it
    }

    fun validate() {
        coroutineScope.launch {
            validateAndAwait()
        }
    }

    fun changeInput(value: INPUT) {
        inputState.value = decorateInput(value)
        validate()
    }

    data class State<INPUT, VALIDATION_RESULT>(
        val input: INPUT,
        val validationResult: VALIDATION_RESULT
    )
}