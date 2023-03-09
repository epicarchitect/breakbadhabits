package breakbadhabits.app.logic.habits.provider

import app.cash.sqldelight.coroutines.asFlow
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import breakbadhabits.app.database.Habit as DatabaseHabit

class HabitProvider(private val appDatabase: AppDatabase) {

    fun provideHabitFlowById(id: Habit.Id) = appDatabase.habitQueries
        .selectById(id.value)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    suspend fun provideHabitById(id: Habit.Id) = withContext(Dispatchers.IO) {
        appDatabase.habitQueries
            .selectById(id.value)
            .executeAsOneOrNull()
            ?.toEntity()
    }

    fun provideHabitsFlow() = appDatabase.habitQueries
        .selectAll()
        .asFlow()
        .map {
            it.executeAsList().map {
                it.toEntity()
            }
        }

    private fun DatabaseHabit.toEntity() = Habit(
        Habit.Id(id),
        Habit.Name(name),
        Habit.IconResource(iconId)
    )
}