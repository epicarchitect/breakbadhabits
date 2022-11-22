package breakbadhabits.feature.habits.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow

abstract class EpicViewModel<STATE>(
    protected val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    abstract val state: StateFlow<STATE>

    fun close() {
        coroutineScope.cancel()
    }
}