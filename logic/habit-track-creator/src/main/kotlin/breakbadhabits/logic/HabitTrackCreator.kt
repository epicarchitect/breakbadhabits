package breakbadhabits.logic

import breakbadhabits.database.AppDatabase
import breakbadhabits.database.IdGenerator
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.datetime.toMillis

class HabitTrackCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator
) {

    suspend fun createHabitTrack(
        habitId: Habit.Id,
        range: HabitTrack.Range,
        dailyCount: HabitTrack.DailyCount,
        comment: HabitTrack.Comment?,
    ) {
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