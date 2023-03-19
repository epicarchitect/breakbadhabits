package breakbadhabits.app.logic.habits.updater

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitTrackUpdater(
    private val appDatabase: AppDatabase
) {
    suspend fun updateHabitTrack(
        id: HabitTrack.Id,
        time: CorrectHabitTrackTime,
        eventCount: CorrectHabitTrackEventCount,
        comment: HabitTrack.Comment?,
    ) = withContext(Dispatchers.IO) {
        appDatabase.habitTrackQueries.update(
            id = id.value,
            startTimeInSecondsUtc = time.data.start.epochSeconds,
            endTimeInSecondsUtc = time.data.endInclusive.epochSeconds,
            dailyCount = eventCount.data.dailyCount.toLong(),
            comment = comment?.value
        )
    }
}