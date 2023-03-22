package breakbadhabits.app.logic.datetime.provider

import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class DateTimeProvider(
    private val configProvider: DateTimeConfigProvider,
    coroutineDispatchers: CoroutineDispatchers
) {
    private val currentTime = MutableStateFlow(getCurrentTime())

    init {
        CoroutineScope(coroutineDispatchers.default).launch {
            while (isActive) {
                currentTime.value = getCurrentTime()
                delay(configProvider.getConfig().timeUpdateDelay)
            }
        }
    }

    fun currentTimeFlow(): Flow<Instant> = currentTime

    fun getCurrentTime() = Clock.System.now()
}