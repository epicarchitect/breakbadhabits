package epicarchitect.breakbadhabits.logic.habits.updater

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.withContext

class HabitWidgetUpdater(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val mainDatabase: MainDatabase,
) {
    suspend fun updateAppWidget(
        id: Int,
        title: String,
        habitIds: List<Int>,
    ) = withContext(coroutineDispatchers.io) {
        mainDatabase.habitWidgetQueries.update(
            id = id,
            title = title,
            habitIds = habitIds
        )
    }
}