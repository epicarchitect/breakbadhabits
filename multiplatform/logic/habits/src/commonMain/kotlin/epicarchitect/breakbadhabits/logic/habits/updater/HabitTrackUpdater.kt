package epicarchitect.breakbadhabits.logic.habits.updater

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackTime
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.withContext

class HabitTrackUpdater(
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun updateHabitTrack(
        id: Int,
        time: CorrectHabitTrackTime,
        eventCount: CorrectHabitTrackEventCount,
        comment: String
    ) = withContext(coroutineDispatchers.io) {
        mainDatabase.habitTrackQueries.update(
            id = id,
            startTime = time.data.start.instant,
            endTime = time.data.endInclusive.instant,
            eventCount = eventCount.data,
            comment = comment
        )
    }
}