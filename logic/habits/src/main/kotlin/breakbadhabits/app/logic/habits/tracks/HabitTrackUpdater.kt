package breakbadhabits.app.logic.habits.tracks

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.HabitTrack
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
            startTimeInSecondsUtc = time.data.start.epochSeconds,
            endTimeInSecondsUtc = time.data.endInclusive.epochSeconds,
            dailyCount = eventCount.data.dailyCount.toLong(),
            comment = comment?.value
        )
    }
}