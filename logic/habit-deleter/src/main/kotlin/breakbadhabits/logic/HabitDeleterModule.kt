package breakbadhabits.logic

import breakbadhabits.entity.Habit
import kotlinx.coroutines.flow.Flow

class HabitDeleterModule(private val delegate: Delegate) {

    fun createHabitIdsProvider() = HabitDeleter(delegate)

    interface Delegate {
        suspend fun deleteHabitById(id: Habit.Id)
        suspend fun deleteHabitTracksByHabitId(id: Habit.Id)
    }
}