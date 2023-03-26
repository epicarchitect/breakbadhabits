package breakbadhabits.app.logic.habits.appWidgetConfig

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAppWidgetConfig
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import breakbadhabits.foundation.coroutines.flow.mapItems
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import breakbadhabits.app.database.HabitAppWidgetConfig as DatabaseHabitAppWidgetConfig

class HabitAppWidgetConfigProvider(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val appDatabase: AppDatabase
) {
    fun provideFlowById(
        id: HabitAppWidgetConfig.Id
    ) = appDatabase.habitAppWidgetConfigQueries.selectById(id.value)
        .asFlow()
        .mapToOneOrNull(coroutineDispatchers.io)
        .map {
            it?.toEntity()
        }.flowOn(coroutineDispatchers.default)

    fun provideFlowByAppWidgetId(
        id: HabitAppWidgetConfig.AppWidgetId
    ) = appDatabase.habitAppWidgetConfigQueries.selectByAppWidgetId(id.value)
        .asFlow()
        .mapToOneOrNull(coroutineDispatchers.io)
        .map {
            it?.toEntity()
        }.flowOn(coroutineDispatchers.default)

    fun provideAllFlow() = appDatabase.habitAppWidgetConfigQueries.selectAll()
        .asFlow()
        .mapToList(coroutineDispatchers.io)
        .mapItems {
            it.toEntity()
        }.flowOn(coroutineDispatchers.io)

    private fun DatabaseHabitAppWidgetConfig.toEntity() = HabitAppWidgetConfig(
        id = HabitAppWidgetConfig.Id(id),
        title = HabitAppWidgetConfig.Title(title),
        appWidgetId = HabitAppWidgetConfig.AppWidgetId(appWidgetId),
        habitIds = habitIds.map(Habit::Id)
    )
}