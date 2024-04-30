package epicarchitect.breakbadhabits.newarch

import kotlinx.coroutines.flow.StateFlow

interface UpdatingValue<T> {
    fun state(): StateFlow<T>
}