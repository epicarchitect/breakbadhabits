package breakbadhabits.app.logic.habit.track.creator

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.framework.datetime.toMillis

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