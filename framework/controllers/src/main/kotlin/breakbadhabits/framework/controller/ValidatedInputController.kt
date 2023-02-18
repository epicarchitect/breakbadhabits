package breakbadhabits.framework.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ValidatedInputController<INPUT, VALIDATION_RESULT>(
    coroutineScope: CoroutineScope,
    initialInput: INPUT,
    validate: suspend (INPUT) -> VALIDATION_RESULT?
) {
    private val inputState = MutableStateFlow(initialInput)
    private val validationResultState = inputState.map(validate).stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    val state = combine(
        inputState,
        validationResultState
    ) { input, result ->
        State(input, result)
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = State(initialInput, null)
    )

    fun changeInput(value: INPUT) {
        inputState.value = value
    }

    class State<INPUT, VALIDATION_RESULT>(
        val input: INPUT,
        val validationResult: VALIDATION_RESULT
    )
}