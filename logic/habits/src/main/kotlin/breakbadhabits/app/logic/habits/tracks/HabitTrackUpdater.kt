package breakbadhabits.app.logic.habits.tracks

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.logic.habits.entity.HabitTrack
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitTrackUpdater(
    private val appDatabase: AppDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun updateHabitTrack(
        id: HabitTrack.Id,
        time: CorrectHabitTrackTime,
        eventCount: CorrectHabitTrackEventCount,
        comment: HabitTrack.Comment?,
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitTrackQueries.update(
            id = id.value,
            startTime = time.data.start,
            endTime = time.data.endInclusive,
            dailyCount = eventCount.data.dailyCount.toLong(),
            comment = comment?.value
        )
    }
}