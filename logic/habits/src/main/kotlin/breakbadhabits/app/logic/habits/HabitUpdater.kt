package breakbadhabits.app.logic.habits

import breakbadhabits.app.database.AppDatabase
import breakbadhabits.app.logic.habits.entity.Habit
import breakbadhabits.foundation.coroutines.CoroutineDispatchers
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
            iconId = icon.value.id.value
        )
    }
}