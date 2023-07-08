package epicarchitect.breakbadhabits.logic.habits.creator

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.icons.Icon
import epicarchitect.breakbadhabits.foundation.identification.IdGenerator
import epicarchitect.breakbadhabits.logic.datetime.provider.DateTimeProvider
import epicarchitect.breakbadhabits.logic.datetime.provider.toInstantBy
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitNewName
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitTrackDateTimeRange
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.withContext

class HabitCreator(
    private val idGenerator: IdGenerator,
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val dateTimeProvider: DateTimeProvider
) {
    suspend fun createHabit(
        name: CorrectHabitNewName,
        icon: Icon,
        trackEventCount: Int,
        trackTime: CorrectHabitTrackDateTimeRange
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
                startTime = trackTime.data.start.toInstantBy(dateTimeProvider),
                endTime = trackTime.data.endInclusive.toInstantBy(dateTimeProvider),
                eventCount = trackEventCount,
                comment = ""
            )
        }
    }
}