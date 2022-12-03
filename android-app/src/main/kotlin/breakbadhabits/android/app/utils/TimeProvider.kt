package breakbadhabits.android.app.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TimeProvider(updatePeriodMillis: Long) {
    private val currentTime = MutableStateFlow(getCurrentTime())

    init {
        CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                currentTime.value = getCurrentTime()
                delay(updatePeriodMillis)
            }
        }
    }

    fun currentTimeFlow() = currentTime

    fun getCurrentTime() = Clock.System.now().toLocalDateTime(
        timeZone = TimeZone.currentSystemDefault()
    )
}