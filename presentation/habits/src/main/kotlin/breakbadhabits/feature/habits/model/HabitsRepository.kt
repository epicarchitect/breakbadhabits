package breakbadhabits.feature.habits.model

import breakbadhabits.entity.Habit
import kolmachikhin.alexander.validation.Correct
import kotlinx.coroutines.flow.Flow

interface HabitsRepository {

    suspend fun insertHabit(
        name: Habit.Name,
        iconResource: Habit.IconResource,
        countability: Habit.Countability
    ): Habit

    suspend fun updateHabit(
        id: Habit.Id,
        name: Habit.Name,
        iconResource: Habit.IconResource
    )

    suspend fun deleteHabit(id: Habit.Id)

    fun habitIdsFlow(): Flow<List<Habit.Id>>

    fun habitFlowById(id: Habit.Id): Flow<Habit?>

    suspend fun getHabitById(id: Habit.Id): Habit?

    suspend fun habitNameExists(name: Habit.Name): Boolean

}