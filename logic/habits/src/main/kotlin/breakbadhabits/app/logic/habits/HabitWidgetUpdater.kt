package breakbadhabits.app.logic.habits

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitWidgetUpdater(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val appDatabase: AppDatabase,
) {
    suspend fun updateAppWidget(
        id: Int,
        title: String,
        habitIds: List<Int>,
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitWidgetQueries.update(
            id = id,
            title = title,
            habitIds = habitIds
        )
    }
}