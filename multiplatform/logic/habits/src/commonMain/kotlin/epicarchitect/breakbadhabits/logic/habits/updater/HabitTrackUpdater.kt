package epicarchitect.breakbadhabits.logic.habits.updater

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.datetime.provider.toInstantBy
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackDateTimeRange
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.withContext

class HabitTrackUpdater(
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val dateTimeProvider: DateTimeProvider
) {
    suspend fun updateHabitTrack(
        id: Int,
        time: CorrectHabitTrackDateTimeRange,
        eventCount: CorrectHabitTrackEventCount,
        comment: String
    ) = withContext(coroutineDispatchers.io) {
        mainDatabase.habitTrackQueries.update(
            id = id,
            startTime = time.data.start.toInstantBy(dateTimeProvider),
            endTime = time.data.endInclusive.toInstantBy(dateTimeProvider),
            eventCount = eventCount.data,
            comment = comment
        )
    }
}