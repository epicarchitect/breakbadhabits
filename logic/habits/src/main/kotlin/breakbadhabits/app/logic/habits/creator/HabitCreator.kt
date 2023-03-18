package breakbadhabits.app.logic.habits.creator

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.datetime.config.DateTimeConfigProvider
import breakbadhabits.app.logic.habits.validator.CorrectHabitNewName
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackRange
import breakbadhabits.foundation.datetime.atEndOfDay
import breakbadhabits.foundation.datetime.atStartOfDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
    private val dateTimeConfigProvider: DateTimeConfigProvider
) {
    suspend fun createHabit(
        name: CorrectHabitNewName,
        icon: Habit.Icon,
        eventCount: CorrectHabitTrackEventCount,
        trackRange: CorrectHabitTrackRange
    ) = withContext(Dispatchers.IO) {
        appDatabase.transaction {
            val timeZone = dateTimeConfigProvider.getConfig().timeZone
            val habitId = idGenerator.nextId()
            val trackId = idGenerator.nextId()

            appDatabase.habitQueries.insert(
                id = habitId,
                name = name.data.value,
                iconId = icon.iconId
            )

            appDatabase.habitTrackQueries.insert(
                id = trackId,
                habitId = habitId,
                startTimeInSeconds = trackRange.data.value.start.atStartOfDay(timeZone).epochSeconds,
                endTimeInSeconds = trackRange.data.value.endInclusive.atEndOfDay(timeZone).epochSeconds,
                dailyCount = eventCount.data.dailyCount.toLong(),
                comment = null
            )
        }
    }
}