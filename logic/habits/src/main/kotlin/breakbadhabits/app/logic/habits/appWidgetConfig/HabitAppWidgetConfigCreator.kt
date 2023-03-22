package breakbadhabits.app.logic.habits.appWidgetConfig

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.database.IdGenerator
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAppWidgetConfig
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitAppWidgetConfigCreator(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val appDatabase: AppDatabase,
    private val idGenerator: IdGenerator,
) {

    suspend fun createAppWidget(
        title: HabitAppWidgetConfig.Title,
        appWidgetId: HabitAppWidgetConfig.AppWidgetId,
        habitIds: List<Habit.Id>,
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitAppWidgetConfigQueries.insert(
            id = idGenerator.nextId(),
            title = title.value,
            appWidgetId = appWidgetId.value,
            habitIds = habitIds.map(Habit.Id::value)
        )
    }
}