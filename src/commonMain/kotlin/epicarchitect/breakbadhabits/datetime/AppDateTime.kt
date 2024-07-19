package epicarchitect.breakbadhabits.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

class AppDateTime {
    fun currentInstant() = Instant.fromEpochSeconds(Clock.System.now().epochSeconds)
    fun currentTimeZone() = TimeZone.currentSystemDefault()
}