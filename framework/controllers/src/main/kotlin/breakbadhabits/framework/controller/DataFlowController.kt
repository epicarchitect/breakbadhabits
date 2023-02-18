package breakbadhabits.framework.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class DataFlowController<DATA>(
    private val coroutineScope: CoroutineScope,
    flow: Flow<DATA>,
    default: () -> DATA
) {
    val state = flow.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = default()
    )
}