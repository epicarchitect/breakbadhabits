package breakbadhabits.framework.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RequestController(
    request: suspend () -> Unit,
    private val coroutineScope: CoroutineScope
) {
    private val internalRequest = request

    private val mutableState = MutableStateFlow<State>(State.NotExecuted())
    val state = mutableState.asStateFlow()

    fun request() {
        coroutineScope.launch {
            mutableState.value = State.Executing()
            internalRequest()
            mutableState.value = State.Executed()
        }
    }

    sealed class State {
        class NotExecuted : State()
        class Executing : State()
        class Executed : State()
    }
}