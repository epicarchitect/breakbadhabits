package epicarchitect.breakbadhabits.logic.habits.updater

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.foundation.icons.Icon
import epicarchitect.breakbadhabits.logic.habits.validator.CorrectHabitNewName
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.withContext

class HabitUpdater(
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun updateHabit(
        habitId: Int,
        habitName: CorrectHabitNewName,
        icon: Icon
    ) = withContext(coroutineDispatchers.io) {
        mainDatabase.habitQueries.update(
            id = habitId,
            name = habitName.data,
            iconId = icon.id
        )
    }
}