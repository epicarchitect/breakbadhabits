package breakbadhabits.app.logic.habits.creator

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.app.logic.datetime.provider.DateTimeProvider
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitTrackCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
    private val dateTimeConfigProvider: DateTimeConfigProvider,
    private val dateTimeProvider: DateTimeProvider
) {

    suspend fun createHabitTrack(
        habitId: Habit.Id,
        range: CorrectHabitTrackTime,
        eventCount: CorrectHabitTrackEventCount,
        comment: HabitTrack.Comment?,
    ) = withContext(Dispatchers.IO) {
        appDatabase.habitTrackQueries.insert(
            id = idGenerator.nextId(),
            habitId = habitId.value,
            startTimeInSecondsUtc = range.data.start.epochSeconds,
            endTimeInSecondsUtc = range.data.endInclusive.epochSeconds,
            dailyCount = eventCount.data.dailyCount.toLong(),
            comment = comment?.value,
            createdInTimeZone = dateTimeConfigProvider.getConfig().appTimeZone.id,
            createdAtTimeInSecondsUtc = dateTimeProvider.getCurrentTime().epochSeconds
        )
    }
}