package epicarchitect.breakbadhabits.entity.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlin.time.Duration


fun <T> updatingStateFlow(
    scope: CoroutineScope,
    delay: () -> Duration,
    value: () -> T
) = flow {
    while (currentCoroutineContext().isActive) {
        delay(delay())
        emit(value())
    }
}.stateIn(scope, SharingStarted.WhileSubscribed(), value())