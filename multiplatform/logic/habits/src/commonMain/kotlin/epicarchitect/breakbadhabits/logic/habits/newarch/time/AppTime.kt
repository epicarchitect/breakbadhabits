package epicarchitect.breakbadhabits.logic.habits.newarch.time

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone

interface AppTime {
    fun instant(): Instant
    fun timeZone(): TimeZone
}