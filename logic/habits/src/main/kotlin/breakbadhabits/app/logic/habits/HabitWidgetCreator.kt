package breakbadhabits.app.logic.habits

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitWidgetCreator(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
) {

    suspend fun createAppWidget(
        title: String,
        systemId: Int,
        habitIds: List<Int>,
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitWidgetQueries.insert(
            id = idGenerator.nextId(),
            title = title,
            systemId = systemId,
            habitIds = habitIds
        )
    }
}