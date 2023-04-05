package breakbadhabits.app.logic.habits

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitDeleter(
    private val appDatabase: AppDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun deleteHabit(id: Int) = withContext(coroutineDispatchers.io) {
        appDatabase.transaction {
            appDatabase.habitQueries.deleteById(id)
            appDatabase.habitTrackQueries.deleteByHabitId(id)
        }
    }
}