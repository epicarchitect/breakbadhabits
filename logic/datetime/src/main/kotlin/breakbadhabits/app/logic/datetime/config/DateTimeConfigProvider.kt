package breakbadhabits.app.logic.datetime.config

import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.TimeZone
import kotlin.time.Duration.Companion.seconds

class DateTimeConfigProvider {

    fun configFlow() = flowOf(getConfig())

    fun getConfig() = defaultConfig()

    private fun defaultConfig() = DateTimeConfig(
        delayDuration = 1.seconds,
        universalTimeZone = TimeZone.UTC,
        systemTimeZone = TimeZone.currentSystemDefault(),
    )
}