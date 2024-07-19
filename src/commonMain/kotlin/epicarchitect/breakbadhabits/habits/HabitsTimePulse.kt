package epicarchitect.breakbadhabits.habits

import epicarchitect.breakbadhabits.datetime.AppDateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.seconds

class HabitsTimePulse(
    coroutineScope: CoroutineScope,
    appDateTime: AppDateTime
) : StateFlow<Instant> by impl(coroutineScope, appDateTime)

private fun impl(coroutineScope: CoroutineScope, appDateTime: AppDateTime) = flow {
    while (currentCoroutineContext().isActive) {
        delay(1.seconds)
        emit(appDateTime.currentInstant())
    }
}.stateIn(coroutineScope, SharingStarted.Eagerly, appDateTime.currentInstant())

