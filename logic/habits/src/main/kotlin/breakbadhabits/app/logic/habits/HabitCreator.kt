package breakbadhabits.app.logic.habits

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.logic.icons.LocalIcon
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitCreator(
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun createHabit(
        name: CorrectHabitNewName,
        icon: LocalIcon,
        trackEventCount: CorrectHabitTrackEventCount,
        trackTime: CorrectHabitTrackTime
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.transaction {
            val habitId = idGenerator.nextId()
            val trackId = idGenerator.nextId()

            appDatabase.habitQueries.insert(
                id = habitId,
                name = name.data,
                iconId = icon.id
            )

            appDatabase.habitTrackQueries.insert(
                id = trackId,
                habitId = habitId,
                startTime = trackTime.data.start,
                endTime = trackTime.data.endInclusive,
                eventCount = trackEventCount.data,
                comment = ""
            )
        }
    }
}