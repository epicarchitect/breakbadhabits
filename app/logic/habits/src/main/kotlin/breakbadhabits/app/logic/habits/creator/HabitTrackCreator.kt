package breakbadhabits.app.logic.habits.creator

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.foundation.datetime.toMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitTrackCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator
) {

    suspend fun createHabitTrack(
        habitId: Habit.Id,
        range: HabitTrack.Range,
        dailyCount: HabitTrack.DailyCount,
        comment: HabitTrack.Comment?,
    ) = withContext(Dispatchers.IO) {
        appDatabase.habitTrackQueries.insert(
            id = idGenerator.nextId(),
            habitId = habitId.value,
            rangeStart = range.value.start.toMillis(),
            rangeEnd = range.value.endInclusive.toMillis(),
            dailyCount = dailyCount.value,
            comment = comment?.value
        )
    }
}