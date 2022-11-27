package breakbadhabits.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class EpicViewModel(
    protected val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {

    fun clear() {
        coroutineScope.cancel()
    }
}

