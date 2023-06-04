package epicarchitect.breakbadhabits.logic.habits.creator

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.identification.IdGenerator
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.withContext

class HabitWidgetCreator(
    private val idGenerator: IdGenerator,
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers,
) {

    suspend fun createAppWidget(
        systemId: Int,
        title: String,
        habitIds: List<Int>,
    ) = withContext(coroutineDispatchers.io) {
        mainDatabase.habitWidgetQueries.insert(
            id = idGenerator.nextId(),
            title = title,
            systemId = systemId,
            habitIds = habitIds
        )
    }
}