package breakbadhabits.app.logic.datetime.config

import kotlinx.datetime.TimeZone
import kotlin.time.Duration

data class DateTimeConfig(
    val delayDuration: Duration,
    val universalTimeZone: TimeZone,
    val systemTimeZone: TimeZone,
)