package epicarchitect.breakbadhabits.infrastructure

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

val Ticks = flow {
    while (currentCoroutineContext().isActive) {
        emit(Clock.System.now())
        delay(1.seconds)
    }
}.stateIn(
    scope = CoroutineScope(Dispatchers.Default),
    started = SharingStarted.WhileSubscribed(5_000),
    initialValue = Clock.System.now()
)
