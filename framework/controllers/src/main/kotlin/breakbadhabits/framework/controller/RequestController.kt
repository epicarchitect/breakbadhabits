package breakbadhabits.framework.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RequestController(
    private val coroutineScope: CoroutineScope,
    request: suspend () -> Unit,
    isAllowedFlow: Flow<Boolean> = flowOf(true),
) {
    private val internalRequest = request

    private val requestState = MutableStateFlow<RequestState>(RequestState.NotExecuted())
    val state = combine(
        requestState,
        isAllowedFlow
    ) { requestState, isAllowed ->
        State(
            isRequestAllowed = isAllowed,
            requestState = requestState
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State(
            isRequestAllowed = false,
            requestState = RequestState.NotExecuted()
        )
    )

    fun request() {
        require(state.value.isRequestAllowed)
        coroutineScope.launch {
            requestState.value = RequestState.Executing()
            internalRequest()
            requestState.value = RequestState.Executed()
        }
    }

    class State(
        val isRequestAllowed: Boolean,
        val requestState: RequestState
    )

    sealed class RequestState {
        class NotExecuted : RequestState()
        class Executing : RequestState()
        class Executed : RequestState()
    }
}