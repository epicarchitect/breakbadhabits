package breakbadhabits.app.logic.habits.deleter

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitTrackDeleter(
    private val appDatabase: AppDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun deleteHabitTrack(habitTrackId: Int) = withContext(coroutineDispatchers.io) {
        appDatabase.transaction {
            appDatabase.habitTrackQueries.deleteById(habitTrackId)
        }
    }
}