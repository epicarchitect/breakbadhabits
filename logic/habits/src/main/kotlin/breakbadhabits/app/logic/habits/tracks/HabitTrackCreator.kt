package breakbadhabits.app.logic.habits.tracks

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitTrackCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    suspend fun createHabitTrack(
        habitId: Habit.Id,
        range: CorrectHabitTrackTime,
        eventCount: CorrectHabitTrackEventCount,
        comment: HabitTrack.Comment?,
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitTrackQueries.insert(
            id = idGenerator.nextId(),
            habitId = habitId.value,
            startTime = range.data.start,
            endTime = range.data.endInclusive,
            dailyCount = eventCount.data.dailyCount.toLong(),
            comment = comment?.value
        )
    }
}