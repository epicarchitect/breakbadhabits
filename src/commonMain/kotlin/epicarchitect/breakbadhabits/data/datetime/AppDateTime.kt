package epicarchitect.breakbadhabits.data.datetime

import epicarchitect.breakbadhabits.operation.sqldelight.updatingStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlin.time.Duration.Companion.seconds

class AppDateTime(coroutineScope: CoroutineScope) {
    val currentInstantState = updatingStateFlow(
        coroutineScope = coroutineScope,
        delay = { 1.seconds },
        value = { Clock.System.now() }
    )
    val currentTimeZoneState = updatingStateFlow(
        coroutineScope = coroutineScope,
        delay = { 1.seconds },
        value = { TimeZone.currentSystemDefault() }
    )
}