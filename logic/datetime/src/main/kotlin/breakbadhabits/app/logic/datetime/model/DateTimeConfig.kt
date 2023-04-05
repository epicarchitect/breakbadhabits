package breakbadhabits.app.logic.datetime.model

import kotlinx.datetime.TimeZone
import kotlin.time.Duration

data class DateTimeConfig(
    val timeUpdateDelay: Duration,
    val appTimeZone: TimeZone,
)