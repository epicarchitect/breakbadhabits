package breakbadhabits.app.logic.habits

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitDeleter(
    private val appDatabase: AppDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun deleteHabit(id: Habit.Id) = withContext(coroutineDispatchers.io) {
        appDatabase.transaction {
            appDatabase.habitQueries.deleteById(id.value)
            appDatabase.habitTrackQueries.deleteByHabitId(id.value)
        }
    }
}