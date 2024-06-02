package epicarchitect.breakbadhabits.data.datetime

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlin.time.Duration.Companion.seconds

class AppDateTime(coroutineScope: CoroutineScope) {
    val currentTimeState = flow {
        while (currentCoroutineContext().isActive) {
            delay(1.seconds)
            emit(currentTime())
        }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), currentTime())

    val currentTimeZoneState = flow {
        while (currentCoroutineContext().isActive) {
            delay(1.seconds)
            emit(currentTimeZone())
        }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), currentTimeZone())

    private fun currentTime() = Instant.fromEpochSeconds(Clock.System.now().epochSeconds)

    private fun currentTimeZone() = TimeZone.currentSystemDefault()
}