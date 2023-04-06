package breakbadhabits.app.logic.habits.updater

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.logic.habits.validator.CorrectHabitNewName
import breakbadhabits.app.logic.icons.LocalIcon
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.withContext

class HabitUpdater(
    private val appDatabase: AppDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun updateHabit(
        habitId: Int,
        habitName: CorrectHabitNewName,
        icon: LocalIcon
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitQueries.update(
            id = habitId,
            name = habitName.data,
            iconId = icon.id
        )
    }
}