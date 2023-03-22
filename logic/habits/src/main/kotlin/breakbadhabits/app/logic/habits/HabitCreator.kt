package breakbadhabits.app.logic.habits

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.app.logic.datetime.DateTimeProvider
import breakbadhabits.app.logic.habits.tracks.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.tracks.CorrectHabitTrackTime
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
    private val dateTimeConfigProvider: DateTimeConfigProvider,
    private val dateTimeProvider: DateTimeProvider,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun createHabit(
        name: CorrectHabitNewName,
        icon: Habit.Icon,
        trackEventCount: CorrectHabitTrackEventCount,
        trackTime: CorrectHabitTrackTime
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.transaction {
            val appTimeZone = dateTimeConfigProvider.getConfig().appTimeZone
            val habitId = idGenerator.nextId()
            val trackId = idGenerator.nextId()
            val createdInTimeZone = appTimeZone.id
            val createdAtTimeInSeconds = dateTimeProvider.getCurrentTime().epochSeconds

            appDatabase.habitQueries.insert(
                id = habitId,
                name = name.data.value,
                iconId = icon.iconId,
                createdInTimeZone = createdInTimeZone,
                createdAtTimeInSecondsUtc = createdAtTimeInSeconds
            )

            appDatabase.habitTrackQueries.insert(
                id = trackId,
                habitId = habitId,
                startTimeInSecondsUtc = trackTime.data.start.epochSeconds,
                endTimeInSecondsUtc = trackTime.data.endInclusive.epochSeconds,
                dailyCount = trackEventCount.data.dailyCount.toLong(),
                comment = null,
                createdInTimeZone = createdInTimeZone,
                createdAtTimeInSecondsUtc = createdAtTimeInSeconds
            )
        }
    }
}