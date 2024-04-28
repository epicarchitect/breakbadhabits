package epicarchitect.breakbadhabits.newarch.time

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

interface AppTime {
    fun instant(): Instant
    fun timeZone(): TimeZone
}