package epicarchitect.breakbadhabits.logic.habits.deleter

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.withContext

class HabitDeleter(
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun deleteHabit(id: Int) = withContext(coroutineDispatchers.io) {
        mainDatabase.transaction {
            mainDatabase.habitQueries.deleteById(id)
            mainDatabase.habitTrackQueries.deleteByHabitId(id)
        }
    }
}
