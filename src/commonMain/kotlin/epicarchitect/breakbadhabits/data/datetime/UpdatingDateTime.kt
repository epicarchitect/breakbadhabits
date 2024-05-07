package epicarchitect.breakbadhabits.data.datetime

import epicarchitect.breakbadhabits.entity.datetime.DateTime
import epicarchitect.breakbadhabits.entity.util.updatingStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

class UpdatingDateTime(
    scope: CoroutineScope,
    delay: () -> Duration,
    value: () -> DateTime
) : StateFlow<DateTime> by updatingStateFlow(scope, delay, value), DateTime {
    override fun instant() = value.instant()
    override fun timeZone() = value.timeZone()
    override fun local() = value.local()
}

