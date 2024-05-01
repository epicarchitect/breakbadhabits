package epicarchitect.breakbadhabits.newarch.time

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface AppTime {
    fun instant(): Instant
    fun timeZone(): TimeZone
}

fun AppTime.dateTime() = instant().toLocalDateTime(timeZone())
fun AppTime.date() = dateTime().date
fun AppTime.time() = dateTime().time
