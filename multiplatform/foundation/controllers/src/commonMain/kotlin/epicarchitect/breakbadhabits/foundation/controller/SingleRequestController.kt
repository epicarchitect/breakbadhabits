package epicarchitect.breakbadhabits.foundation.controller

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SingleRequestController(
    override val coroutineScope: CoroutineScope,
    isAllowedFlow: Flow<Boolean> = flowOf(true),
    request: suspend () -> Unit
) : Controller<SingleRequestController.State> {
    private val internalRequest = request

    private val requestState = MutableStateFlow<RequestState>(RequestState.NotExecuted())
    override val state = combine(
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

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun request() {
        coroutineScope.launch {
            try {
                requestState.value = RequestState.Executing()
                require(state.value.isRequestAllowed)
                internalRequest()
                requestState.value = RequestState.Executed()
            } catch (e: Exception) {
                requestState.value = RequestState.NotExecuted()
            }
        }
    }

    data class State(
        val isRequestAllowed: Boolean,
        val requestState: RequestState
    )

    sealed interface RequestState {
        class NotExecuted : RequestState
        class Executing : RequestState
        class Executed : RequestState
    }
}

fun CoroutineScopeOwner.SingleRequestController(
    isAllowedFlow: Flow<Boolean> = flowOf(true),
    request: suspend () -> Unit = {}
) = SingleRequestController(
    coroutineScope,
    isAllowedFlow,
    request
)