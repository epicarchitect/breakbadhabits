package breakbadhabits.app.dependencies.main.utils

import breakbadhabits.feature.habits.model.TimeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class TimeProviderImpl(updatePeriodMillis: Long) : TimeProvider {
    private val currentTime = MutableStateFlow(getCurrentTime())

    init {
        CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                currentTime.value = getCurrentTime()
                delay(updatePeriodMillis)
            }
        }
    }

    override fun currentTimeFlow() = currentTime

    override fun getCurrentTime() = Clock.System.now().toLocalDateTime(
        timeZone = TimeZone.currentSystemDefault()
    )
}