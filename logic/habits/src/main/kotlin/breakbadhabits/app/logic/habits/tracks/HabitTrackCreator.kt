package breakbadhabits.app.logic.habits.tracks

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.app.logic.datetime.DateTimeProvider
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitTrackCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
    private val dateTimeConfigProvider: DateTimeConfigProvider,
    private val dateTimeProvider: DateTimeProvider,
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
            startTimeInSecondsUtc = range.data.start.epochSeconds,
            endTimeInSecondsUtc = range.data.endInclusive.epochSeconds,
            dailyCount = eventCount.data.dailyCount.toLong(),
            comment = comment?.value,
            createdInTimeZone = dateTimeConfigProvider.getConfig().appTimeZone.id,
            createdAtTimeInSecondsUtc = dateTimeProvider.getCurrentTime().epochSeconds
        )
    }
}