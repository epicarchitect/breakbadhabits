package epicarchitect.breakbadhabits.logic.datetime.provider

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.datetime.ZonedDateTime
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlin.time.Duration.Companion.seconds

class DateTimeProvider(
    private val coroutineDispatchers: CoroutineDispatchers
) {

    fun currentDateTimeFlow() = updatingFlow(::getCurrentDateTime)

    fun currentTimeZoneFlow() = updatingFlow(::getCurrentTimeZone)

    fun getCurrentDateTime() = ZonedDateTime(
        instant = getCurrentInstant(),
        timeZone = getCurrentTimeZone()
    )

    fun getCurrentInstant() = Clock.System.now()

    fun getCurrentTimeZone() = TimeZone.currentSystemDefault()

    private fun <T> updatingFlow(value: () -> T) = flow {
        while (currentCoroutineContext().isActive) {
            emit(value())
            delay(UPDATE_DELAY)
        }
    }.distinctUntilChanged().flowOn(coroutineDispatchers.default)

    companion object {
        private val UPDATE_DELAY = 1.seconds
    }
}