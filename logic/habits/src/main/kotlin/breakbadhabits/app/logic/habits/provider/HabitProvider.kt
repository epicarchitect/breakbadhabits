package breakbadhabits.app.logic.habits.provider

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.logic.habits.model.Habit
import breakbadhabits.app.logic.icons.LocalIconProvider
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.foundation.coroutines.flow.mapItems
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class HabitProvider(
    private val appDatabase: AppDatabase,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val localIconProvider: LocalIconProvider
) {

    fun habitFlow(id: Int) = appDatabase.habitQueries
        .selectById(id)
        .asFlow()
        .mapToOneOrNull(coroutineDispatchers.io)
        .map {
            it?.toEntity()
        }.flowOn(coroutineDispatchers.io)

    fun habitsFlow() = appDatabase.habitQueries
        .selectAll()
        .asFlow()
        .mapToList(coroutineDispatchers.io)
        .mapItems {
            it.toEntity()
        }.flowOn(coroutineDispatchers.default)

    private fun breakbadhabits.app.database.Habit.toEntity() = Habit(
        id = id,
        name = name,
        icon = localIconProvider.getIcon(iconId)
    )
}