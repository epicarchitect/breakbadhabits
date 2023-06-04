package epicarchitect.breakbadhabits.foundation.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class LoadingController<DATA>(
    coroutineScope: CoroutineScope,
    flow: Flow<DATA>
) : StateController<LoadingController.State<out DATA>> {

    override val state = flow.map {
        State.Loaded(it)
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Eagerly,
        initialValue = State.Loading
    )

    sealed class State<DATA> {
        object Loading : State<Nothing>()
        data class Loaded<DATA>(val data: DATA) : State<DATA>()
    }
}