package breakbadhabits.app.logic.habits.updater

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.logic.habits.validator.CorrectHabitNewName
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HabitUpdater(
    private val appDatabase: AppDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun updateHabit(
        habitId: Habit.Id,
        habitName: CorrectHabitNewName,
        icon: Habit.Icon
    ) = withContext(coroutineDispatchers.io) {
        appDatabase.habitQueries.update(
            id = habitId.value,
            name = habitName.data.value,
            iconId = icon.iconId
        )
    }
}