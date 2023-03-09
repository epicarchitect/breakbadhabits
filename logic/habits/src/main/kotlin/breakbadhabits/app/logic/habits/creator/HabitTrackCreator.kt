package breakbadhabits.app.logic.habits.creator

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.habits.serializer.HabitTrackSerializer
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackRange
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
        range: CorrectHabitTrackRange,
        eventCount: CorrectHabitTrackEventCount,
        comment: HabitTrack.Comment?,
    ) = withContext(Dispatchers.IO) {
        appDatabase.habitTrackQueries.insert(
            id = idGenerator.nextId(),
            habitId = habitId.value,
            rangeStart = range.data.value.start.toMillis(),
            rangeEnd = range.data.value.endInclusive.toMillis(),
            eventCount = eventCount.data.value.toLong(),
            eventCountTimeUnit = habitTrackSerializer.encodeEventCountTimeUnit(eventCount.data.timeUnit),
            comment = comment?.value
        )
    }
}