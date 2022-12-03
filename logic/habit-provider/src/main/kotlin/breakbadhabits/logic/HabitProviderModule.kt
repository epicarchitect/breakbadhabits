package breakbadhabits.logic

import breakbadhabits.entity.Habit
import kotlinx.coroutines.flow.Flow

class HabitProviderModule(private val delegate: Delegate) {

    fun createHabitProvider() = HabitProvider(delegate)

    interface Delegate {
        fun habitFlow(id: Habit.Id): Flow<Habit?>
        suspend fun getHabit(id: Habit.Id): Habit?
    }
}