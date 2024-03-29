package epicarchitect.breakbadhabits.foundation.controller

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineScopeOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DataFlowController<DATA>(
    override val coroutineScope: CoroutineScope,
    flow: Flow<DATA>
) : Controller<DataFlowController.State<out DATA>> {

    override val state = flow.map {
        State.Loaded(it)
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State.Loading
    )

    sealed class State<DATA> {
        data object Loading : State<Nothing>()
        data class Loaded<DATA>(val data: DATA) : State<DATA>()
    }
}

fun <DATA> CoroutineScopeOwner.DataFlowController(
    flow: Flow<DATA>
) = DataFlowController(coroutineScope, flow)