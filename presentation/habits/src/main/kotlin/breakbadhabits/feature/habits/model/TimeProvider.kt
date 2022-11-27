package breakbadhabits.feature.habits.model

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface TimeProvider {
    fun currentTimeFlow(): Flow<LocalDateTime>
    fun getCurrentTime(): LocalDateTime
}