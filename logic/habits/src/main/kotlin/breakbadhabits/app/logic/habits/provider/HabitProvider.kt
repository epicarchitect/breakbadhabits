package breakbadhabits.app.logic.habits.provider

import app.cash.sqldelight.coroutines.asFlow
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.foundation.datetime.secondsToInstant
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import breakbadhabits.app.database.Habit as DatabaseHabit

class HabitProvider(
    private val appDatabase: AppDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {

    fun habitFlow(id: Habit.Id) = appDatabase.habitQueries
        .selectById(id.value)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }.flowOn(coroutineDispatchers.io)

    suspend fun getHabit(id: Habit.Id) = withContext(coroutineDispatchers.io) {
        appDatabase.habitQueries
            .selectById(id.value)
            .executeAsOneOrNull()
            ?.toEntity()
    }

    fun habitsFlow() = appDatabase.habitQueries
        .selectAll()
        .asFlow()
        .map {
            it.executeAsList().map {
                it.toEntity()
            }
        }.flowOn(coroutineDispatchers.io)

    private fun DatabaseHabit.toEntity() = Habit(
        id = Habit.Id(id),
        name = Habit.Name(name),
        icon = Habit.Icon(iconId),
        creationTime = Habit.CreationTime(
            time = createdAtTimeInSecondsUtc.secondsToInstant(),
            timeZone = TimeZone.of(createdInTimeZone)
        )
    )
}