package breakbadhabits.app.logic.habits

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitTrackUpdater(
    private val appDatabase: AppDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun updateHabitTrack(
        id: Int,
        time: CorrectHabitTrackTime,
        eventCount: CorrectHabitTrackEventCount,
        comment: String,
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitTrackQueries.update(
            id = id,
            startTime = time.data.start,
            endTime = time.data.endInclusive,
            eventCount = eventCount.data,
            comment = comment
        )
    }
}