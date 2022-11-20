package breakbadhabits.android.app.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object TikTik {

    private val currentTime = MutableStateFlow(System.currentTimeMillis())

    init {
        CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                currentTime.emit(System.currentTimeMillis())
                delay(1000)
            }
        }
    }

    fun everySecond(action: (Long) -> Unit = {}): Flow<Long> {
        return currentTime.onEach { action(it) }
    }
}