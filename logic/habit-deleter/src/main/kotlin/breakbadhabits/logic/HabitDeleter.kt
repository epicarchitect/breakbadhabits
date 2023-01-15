package breakbadhabits.logic

import breakbadhabits.database.AppDatabase
import breakbadhabits.entity.Habit

class HabitDeleter(private val appDatabase: AppDatabase) {
    suspend fun deleteById(id: Habit.Id) {
        appDatabase.transaction {
            appDatabase.habitQueries.deleteById(id.value)
            appDatabase.habitTrackQueries.deleteByHabitId(id.value)
        }
    }
}