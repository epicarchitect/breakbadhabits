package breakbadhabits.app.logic.habits.creator

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitTrackCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator
) {

    suspend fun createHabitTrack(
        habitId: Habit.Id,
        range: CorrectHabitTrackRange,
        eventCount: CorrectHabitTrackEventCount,
        comment: HabitTrack.Comment?,
    ) = withContext(Dispatchers.IO) {
        appDatabase.habitTrackQueries.insert(
            id = idGenerator.nextId(),
            habitId = habitId.value,
            startTimeInSeconds = range.data.value.start.epochSeconds,
            endTimeInSeconds = range.data.value.endInclusive.epochSeconds,
            dailyCount = eventCount.data.dailyCount.toLong(),
            comment = comment?.value
        )
    }
}