package breakbadhabits.app.logic.habits.deleter

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitDeleter(private val appDatabase: AppDatabase) {
    suspend fun deleteById(id: Habit.Id) = withContext(Dispatchers.IO) {
        appDatabase.transaction {
            appDatabase.habitQueries.deleteById(id.value)
            appDatabase.habitTrackQueries.deleteByHabitId(id.value)
        }
    }
}