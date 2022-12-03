package breakbadhabits.logic

import breakbadhabits.entity.Habit
import kotlinx.coroutines.flow.Flow

class HabitIdsProviderModule(private val delegate: Delegate) {

    fun createHabitIdsProvider() = HabitIdsProvider(delegate)

    interface Delegate {
        fun habitIdsFlow(): Flow<List<Habit.Id>>
    }
}