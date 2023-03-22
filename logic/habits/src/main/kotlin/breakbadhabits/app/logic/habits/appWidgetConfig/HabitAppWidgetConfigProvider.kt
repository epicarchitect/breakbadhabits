package breakbadhabits.app.logic.habits.appWidgetConfig

import app.cash.sqldelight.coroutines.asFlow
import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAppWidgetConfig
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
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
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }.flowOn(coroutineDispatchers.io)

    fun provideFlowByAppWidgetId(
        id: HabitAppWidgetConfig.AppWidgetId
    ) = appDatabase.habitAppWidgetConfigQueries.selectByAppWidgetId(id.value)
        .asFlow()
        .map {
            it.executeAsOneOrNull()?.toEntity()
        }.flowOn(coroutineDispatchers.io)

    fun provideAllFlow() = appDatabase.habitAppWidgetConfigQueries.selectAll()
        .asFlow()
        .map {
            it.executeAsList().map {
                it.toEntity()
            }
        }.flowOn(coroutineDispatchers.io)

    private fun DatabaseHabitAppWidgetConfig.toEntity() = HabitAppWidgetConfig(
        id = HabitAppWidgetConfig.Id(id),
        title = HabitAppWidgetConfig.Title(title),
        appWidgetId = HabitAppWidgetConfig.AppWidgetId(appWidgetId),
        habitIds = habitIds.map(Habit::Id)
    )
}