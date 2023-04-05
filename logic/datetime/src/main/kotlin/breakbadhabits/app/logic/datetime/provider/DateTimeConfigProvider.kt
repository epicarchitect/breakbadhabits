package breakbadhabits.app.logic.datetime.provider

import breakbadhabits.app.logic.datetime.model.DateTimeConfig
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.TimeZone
import kotlin.time.Duration.Companion.seconds

class DateTimeConfigProvider {

    fun configFlow() = flowOf(getConfig())

    fun getConfig() = defaultConfig()

    private fun defaultConfig() = DateTimeConfig(
        timeUpdateDelay = 1.seconds,
        appTimeZone = TimeZone.currentSystemDefault()
    )
}