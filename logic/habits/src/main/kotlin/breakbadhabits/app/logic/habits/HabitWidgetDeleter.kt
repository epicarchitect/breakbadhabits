package breakbadhabits.app.logic.habits

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitWidgetDeleter(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val appDatabase: AppDatabase
) {
    suspend fun deleteById(
        id: Int
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitWidgetQueries.deleteById(id)
    }

    suspend fun deleteBySystemId(
        systemId: Int
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitWidgetQueries.deleteBySystemId(systemId)
    }

    suspend fun deleteBySystemIds(
        systemIds: List<Int>
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.transaction {
            systemIds.forEach(appDatabase.habitWidgetQueries::deleteBySystemId)
        }
    }
}