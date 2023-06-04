package epicarchitect.breakbadhabits.logic.habits.creator

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.icons.Icon
import epicarchitect.breakbadhabits.foundation.identification.IdGenerator
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitNewName
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackTime
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.withContext

class HabitCreator(
    private val idGenerator: IdGenerator,
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun createHabit(
        name: CorrectHabitNewName,
        icon: Icon,
        trackEventCount: Int,
        trackTime: CorrectHabitTrackTime
    ) = withContext(coroutineDispatchers.io) {
        mainDatabase.transaction {
            val habitId = idGenerator.nextId()
            val trackId = idGenerator.nextId()

            mainDatabase.habitQueries.insert(
                id = habitId,
                name = name.data,
                iconId = icon.id
            )

            mainDatabase.habitTrackQueries.insert(
                id = trackId,
                habitId = habitId,
                startTime = trackTime.data.start.instant,
                endTime = trackTime.data.endInclusive.instant,
                eventCount = trackEventCount,
                comment = ""
            )
        }
    }
}