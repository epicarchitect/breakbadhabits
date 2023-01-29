package breakbadhabits.app.logic.habit.deleter

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit

class HabitDeleter(private val appDatabase: AppDatabase) {
    suspend fun deleteById(id: Habit.Id) {
        appDatabase.transaction {
            appDatabase.habitQueries.deleteById(id.value)
            appDatabase.habitTrackQueries.deleteByHabitId(id.value)
        }
    }
}