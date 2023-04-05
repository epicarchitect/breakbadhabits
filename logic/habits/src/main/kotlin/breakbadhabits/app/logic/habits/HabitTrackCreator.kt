package breakbadhabits.app.logic.habits

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitTrackCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    suspend fun createHabitTrack(
        habitId: Int,
        range: CorrectHabitTrackTime,
        eventCount: CorrectHabitTrackEventCount,
        comment: String,
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitTrackQueries.insert(
            id = idGenerator.nextId(),
            habitId = habitId,
            startTime = range.data.start,
            endTime = range.data.endInclusive,
            eventCount = eventCount.data,
            comment = comment
        )
    }
}