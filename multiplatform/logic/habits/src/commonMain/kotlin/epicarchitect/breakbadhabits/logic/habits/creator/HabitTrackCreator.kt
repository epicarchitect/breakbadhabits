package epicarchitect.breakbadhabits.logic.habits.creator

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.identification.IdGenerator
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackTime
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.withContext

class HabitTrackCreator(
    private val idGenerator: IdGenerator,
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    suspend fun createHabitTrack(
        habitId: Int,
        range: CorrectHabitTrackTime,
        eventCount: CorrectHabitTrackEventCount,
        comment: String,
    ) = withContext(coroutineDispatchers.io) {
        mainDatabase.habitTrackQueries.insert(
            id = idGenerator.nextId(),
            habitId = habitId,
            startTime = range.data.start.instant,
            endTime = range.data.endInclusive.instant,
            eventCount = eventCount.data,
            comment = comment
        )
    }
}