package breakbadhabits.app.logic.habits.creator

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.habits.validator.CorrectHabitNewName
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackEventCount
import breakbadhabits.app.logic.habits.validator.CorrectHabitTrackRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator
) {
    suspend fun createHabit(
        name: CorrectHabitNewName,
        icon: Habit.Icon,
        firstTrackValue: CorrectHabitTrackEventCount,
        firstTrackInterval: CorrectHabitTrackRange
    ) = withContext(Dispatchers.IO) {
        appDatabase.transaction {
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
                startTimeInSeconds = firstTrackInterval.data.value.start.epochSeconds,
                endTimeInSeconds = firstTrackInterval.data.value.endInclusive.epochSeconds,
                dailyCount = firstTrackValue.data.dailyCount.toLong(),
                comment = null
            )
        }
    }
}