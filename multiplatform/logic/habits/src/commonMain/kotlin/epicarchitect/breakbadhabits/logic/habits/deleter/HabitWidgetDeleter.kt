package epicarchitect.breakbadhabits.logic.habits.deleter

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.withContext

class HabitWidgetDeleter(
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun deleteById(
        id: Int
    ) = withContext(coroutineDispatchers.io) {
        mainDatabase.habitWidgetQueries.deleteById(id)
    }

    suspend fun deleteBySystemId(
        systemId: Int
    ) = withContext(coroutineDispatchers.io) {
        mainDatabase.habitWidgetQueries.deleteBySystemId(systemId)
    }

    suspend fun deleteBySystemIds(
        systemIds: List<Int>
    ) = withContext(coroutineDispatchers.io) {
        mainDatabase.transaction {
            systemIds.forEach(mainDatabase.habitWidgetQueries::deleteBySystemId)
        }
    }
}