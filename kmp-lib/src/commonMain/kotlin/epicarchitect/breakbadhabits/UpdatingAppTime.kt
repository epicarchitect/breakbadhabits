package epicarchitect.breakbadhabits

import epicarchitect.breakbadhabits.newarch.UpdatingValue
import epicarchitect.breakbadhabits.newarch.DefaultUpdatingValue
import epicarchitect.breakbadhabits.newarch.time.AppTime
import epicarchitect.breakbadhabits.newarch.time.CachedAppTime
import epicarchitect.breakbadhabits.newarch.time.SystemAppTime
import kotlin.time.Duration.Companion.seconds

val UpdatingAppTime: UpdatingValue<AppTime> = DefaultUpdatingValue(
    updateDelay = 1.seconds,
    getValue = { CachedAppTime(SystemAppTime()) }
)