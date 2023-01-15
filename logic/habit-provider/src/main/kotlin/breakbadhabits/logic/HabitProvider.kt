package breakbadhabits.logic

import breakbadhabits.database.AppDatabase
import breakbadhabits.entity.Habit
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.map

class HabitProvider(private val appDatabase: AppDatabase) {

    fun provideHabitFlowById(id: Habit.Id) = appDatabase.habitQueries
        .selectById(id.value)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }

    suspend fun provideHabitById(id: Habit.Id) = appDatabase.habitQueries
        .selectById(id.value)
        .executeAsOneOrNull()
        ?.toEntity()

    fun provideHabitsFlow() = appDatabase.habitQueries
        .selectAll()
        .asFlow()
        .map {
            it.executeAsList().map {
                it.toEntity()
            }
        }

    private fun breakbadhabits.database.Habit.toEntity() = Habit(
        Habit.Id(id),
        Habit.Name(name),
        Habit.IconResource(iconId),
        Habit.Countability(isCountable)
    )
}