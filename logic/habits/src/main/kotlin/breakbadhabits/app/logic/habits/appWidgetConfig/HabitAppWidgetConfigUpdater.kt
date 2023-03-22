package breakbadhabits.app.logic.habits.appWidgetConfig

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitAppWidgetConfig
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitAppWidgetConfigUpdater(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val appDatabase: AppDatabase,
) {
    suspend fun updateAppWidget(
        id: HabitAppWidgetConfig.Id,
        title: HabitAppWidgetConfig.Title,
        habitIds: List<Habit.Id>,
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitAppWidgetConfigQueries.update(
            id = id.value,
            title = title.value,
            habitIds = habitIds.map(Habit.Id::value)
        )
    }
}