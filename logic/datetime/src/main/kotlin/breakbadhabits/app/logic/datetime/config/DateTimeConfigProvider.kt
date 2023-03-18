package breakbadhabits.app.logic.datetime.config

import kotlinx.coroutines.flow.flowOf
import kotlin.time.Duration.Companion.seconds

class DateTimeConfigProvider {

    fun configFlow() = flowOf(defaultConfig)

    fun getConfig() = defaultConfig

    companion object {
        private val defaultConfig = DateTimeConfig(
            delayDuration = 1L.seconds
        )
    }
}