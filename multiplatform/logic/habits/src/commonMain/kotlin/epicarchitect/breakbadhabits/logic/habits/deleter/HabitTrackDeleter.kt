package epicarchitect.breakbadhabits.logic.habits.deleter

import epicarchitect.breakbadhabits.foundation.coroutines.CoroutineDispatchers
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase
import kotlinx.coroutines.withContext

class HabitTrackDeleter(
    private val mainDatabase: MainDatabase,
    private val coroutineDispatchers: CoroutineDispatchers
) {
    suspend fun deleteHabitTrack(habitTrackId: Int) = withContext(coroutineDispatchers.io) {
        mainDatabase.transaction {
            mainDatabase.habitTrackQueries.deleteById(habitTrackId)
        }
    }
}
