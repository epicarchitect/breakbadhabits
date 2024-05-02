package epicarchitect.breakbadhabits.entity.util

import kotlinx.coroutines.flow.StateFlow

interface UpdatingValue<T> {
    fun state(): StateFlow<T>
}