package breakbadhabits.app.logic.habits.deleter

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.HabitTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitTrackDeleter(private val appDatabase: AppDatabase) {
    suspend fun deleteHabitTrack(id: HabitTrack.Id) = withContext(Dispatchers.IO) {
        appDatabase.transaction {
            appDatabase.habitTrackQueries.deleteById(id.value)
        }
    }
}