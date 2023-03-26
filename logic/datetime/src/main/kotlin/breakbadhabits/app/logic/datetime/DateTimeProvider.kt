package breakbadhabits.app.logic.datetime

import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class DateTimeProviderImpl(
    private val configProvider: DateTimeConfigProvider,
    coroutineDispatchers: CoroutineDispatchers
) : DateTimeProvider {
    override val currentTime = MutableStateFlow(Clock.System.now())

    init {
        CoroutineScope(coroutineDispatchers.default).launch {
            while (isActive) {
                currentTime.value = Clock.System.now()
                delay(configProvider.getConfig().timeUpdateDelay)
            }
        }
    }
}

interface DateTimeProvider {
    val currentTime: StateFlow<Instant>
}