package breakbadhabits.app.logic.habits.appWidgetConfig

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.logic.habits.entity.HabitAppWidgetConfig
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitAppWidgetConfigDeleter(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val appDatabase: AppDatabase
) {
    suspend fun deleteById(
        id: HabitAppWidgetConfig.Id
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitAppWidgetConfigQueries.deleteById(id.value)
    }

    suspend fun deleteByAppWidgetId(
        appWidgetId: HabitAppWidgetConfig.AppWidgetId
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitAppWidgetConfigQueries.deleteByWidgetId(appWidgetId.value)
    }

    suspend fun deleteByAppWidgetIds(
        appWidgetIds: List<HabitAppWidgetConfig.AppWidgetId>
    ) = withContext(coroutineDispatchers.io) {
        appWidgetIds.forEach {
            appDatabase.habitAppWidgetConfigQueries.deleteByWidgetId(it.value)
        }
    }
}