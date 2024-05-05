package epicarchitect.breakbadhabits.entity.datetime

import epicarchitect.breakbadhabits.entity.util.DefaultUpdatingValue
import epicarchitect.breakbadhabits.entity.util.UpdatingValue
import kotlin.time.Duration.Companion.seconds

private val impl: UpdatingValue<AppTime> = DefaultUpdatingValue(
    updateDelay = 1.seconds,
    getValue = { CachedAppTime(SystemAppTime()) }
)

object UpdatingAppTime : UpdatingValue<AppTime> by impl, AppTime {
    override fun instant() = state().value.instant()
    override fun timeZone() = state().value.timeZone()
}
