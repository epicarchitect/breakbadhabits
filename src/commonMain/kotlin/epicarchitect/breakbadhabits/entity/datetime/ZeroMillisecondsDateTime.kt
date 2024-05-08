package epicarchitect.breakbadhabits.entity.datetime

import kotlinx.datetime.Instant
import kotlinx.datetime.toLocalDateTime

class ZeroMillisecondsDateTime(
    private val base: DateTime
) : DateTime {
    override fun instant() = Instant.fromEpochSeconds(base.instant().epochSeconds)
    override fun local() = instant().toLocalDateTime(timeZone())
    override fun timeZone() = base.timeZone()
}