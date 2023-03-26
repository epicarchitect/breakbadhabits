package breakbadhabits.app.logic.habits.tracks

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.logic.habits.entity.HabitTrack
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitTrackDeleter(
    private val appDatabase: AppDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun deleteHabitTrack(id: HabitTrack.Id) = withContext(coroutineDispatchers.io) {
        appDatabase.transaction {
            appDatabase.habitTrackQueries.deleteById(id.value)
        }
    }
}