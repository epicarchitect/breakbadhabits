package breakbadhabits.app.logic.habits

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.logic.habits.entity.Habit
import breakbadhabits.app.logic.icons.LocalIcon
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

    fun habitFlow(id: Habit.Id) = appDatabase.habitQueries
        .selectById(id.value)
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
        id = Habit.Id(id),
        name = Habit.Name(name),
        icon = Habit.Icon(localIconProvider.getIcon(LocalIcon.Id(iconId)))
    )
}