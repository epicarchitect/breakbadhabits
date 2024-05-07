package epicarchitect.breakbadhabits.entity.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

interface DateTime {
    fun instant(): Instant
    fun local(): LocalDateTime
    fun timeZone(): TimeZone
}
