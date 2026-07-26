package epicarchitect.breakbadhabits.datetime

import kotlinx.datetime.TimeZone
import kotlin.time.Clock.System
import kotlin.time.Instant

class AppDateTime {
    fun currentInstant() = Instant.fromEpochSeconds(System.now().epochSeconds)
    fun currentTimeZone() = TimeZone.currentSystemDefault()
}