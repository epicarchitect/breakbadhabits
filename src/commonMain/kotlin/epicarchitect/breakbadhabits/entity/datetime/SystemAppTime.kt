package epicarchitect.breakbadhabits.entity.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone

class SystemAppTime : AppTime {
    override fun instant() = Clock.System.now()
    override fun timeZone() = TimeZone.currentSystemDefault()
}