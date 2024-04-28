package epicarchitect.breakbadhabits.logic.datetime.provider

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.seconds

private val UPDATE_DELAY = 1.seconds

class DateTimeProvider(
    private val coroutineDispatchers: CoroutineDispatchers
) {
    private val updatingInstant = updatingStateFlow(::getCurrentInstant)
    private val updatingTimeZone = updatingStateFlow(::getCurrentTimeZone)

    fun currentInstantFlow(): Flow<Instant> = updatingInstant
    fun currentTimeZoneFlow(): Flow<TimeZone> = updatingTimeZone

    fun getCurrentInstant() = Clock.System.now()

    fun getCurrentTimeZone() = TimeZone.currentSystemDefault()

    private fun <T> updatingStateFlow(value: () -> T) = flow {
        while (currentCoroutineContext().isActive) {
            emit(value())
            delay(UPDATE_DELAY)
        }
    }.stateIn(
        scope = CoroutineScope(coroutineDispatchers.default),
        started = SharingStarted.WhileSubscribed(),
        initialValue = value()
    )

    data class State(
        val instant: Instant,
        val timeZone: TimeZone
    )
}

fun <T> DateTimeProvider.withCurrentInstantAndTimeZone(
    transform: (Instant, TimeZone) -> T
) = combine(currentInstantFlow(), currentTimeZoneFlow(), transform)

fun DateTimeProvider.currentDateTimeFlow() = combine(
    currentInstantFlow(),
    currentTimeZoneFlow()
) { instant, timeZone ->
    instant.toLocalDateTime(timeZone)
}

fun DateTimeProvider.getCurrentDateTime() = getCurrentInstant().toLocalDateTime(getCurrentTimeZone())

fun LocalDateTime.toInstantBy(provider: DateTimeProvider) = toInstant(provider.getCurrentTimeZone())