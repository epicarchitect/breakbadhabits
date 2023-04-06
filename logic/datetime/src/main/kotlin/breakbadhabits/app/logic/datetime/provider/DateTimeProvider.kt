package breakbadhabits.app.logic.datetime.provider

import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlin.time.Duration.Companion.seconds

interface DateTimeProvider {
    val currentTime: StateFlow<Instant>
    val timeZone: StateFlow<TimeZone>
}

class DateTimeProviderImpl(
    coroutineDispatchers: CoroutineDispatchers
) : DateTimeProvider {
    private val coroutineScope = CoroutineScope(coroutineDispatchers.default)

    override val currentTime = MutableStateFlow(Clock.System.now())
    override val timeZone = MutableStateFlow(TimeZone.currentSystemDefault())

    init {
        coroutineScope.launch {
            while (isActive) {
                currentTime.value = Clock.System.now()
                delay(TIME_UPDATE_DELAY)
            }
        }
    }

    companion object {
        private val TIME_UPDATE_DELAY = 1.seconds
    }
}