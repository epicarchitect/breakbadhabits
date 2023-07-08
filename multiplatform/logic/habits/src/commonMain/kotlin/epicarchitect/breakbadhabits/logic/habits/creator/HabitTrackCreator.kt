package epicarchitect.breakbadhabits.logic.habits.creator

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.identification.IdGenerator
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.datetime.provider.toInstantBy
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackDateTimeRange
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackEventCount
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.withContext

class HabitTrackCreator(
    private val idGenerator: IdGenerator,
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val dateTimeProvider: DateTimeProvider
) {

    suspend fun createHabitTrack(
        habitId: Int,
        range: CorrectHabitTrackDateTimeRange,
        eventCount: CorrectHabitTrackEventCount,
        comment: String
    ) = withContext(coroutineDispatchers.io) {
        mainDatabase.habitTrackQueries.insert(
            id = idGenerator.nextId(),
            habitId = habitId,
            startTime = range.data.start.toInstantBy(dateTimeProvider),
            endTime = range.data.endInclusive.toInstantBy(dateTimeProvider),
            eventCount = eventCount.data,
            comment = comment
        )
    }
}