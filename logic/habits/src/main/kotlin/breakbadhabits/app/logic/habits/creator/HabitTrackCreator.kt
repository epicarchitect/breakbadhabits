package breakbadhabits.app.logic.habits.creator

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.serializer.HabitTrackSerializer
import breakbadhabits.foundation.datetime.toMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitTrackCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
    private val habitTrackSerializer: HabitTrackSerializer
) {

    suspend fun createHabitTrack(
        habitId: Habit.Id,
        range: HabitTrack.Range,
        eventCount: HabitTrack.EventCount,
        comment: HabitTrack.Comment?,
    ) = withContext(Dispatchers.IO) {
        appDatabase.habitTrackQueries.insert(
            id = idGenerator.nextId(),
            habitId = habitId.value,
            rangeStart = range.value.start.toMillis(),
            rangeEnd = range.value.endInclusive.toMillis(),
            eventCount = eventCount.value.toLong(),
            eventCountTimeUnit = habitTrackSerializer.encodeEventCountTimeUnit(eventCount.timeUnit),
            comment = comment?.value
        )
    }
}