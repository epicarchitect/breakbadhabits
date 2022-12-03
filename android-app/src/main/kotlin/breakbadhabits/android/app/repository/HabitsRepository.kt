package breakbadhabits.android.app.repository

import breakbadhabits.android.app.database.HabitsDao
import breakbadhabits.entity.Habit
import breakbadhabits.extension.coroutines.flow.mapItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import breakbadhabits.android.app.database.Habit as DatabaseHabit

class HabitsRepository(
    private val idGenerator: IdGenerator,
    private val habitsDao: HabitsDao
) {

    suspend fun insertHabit(
        name: Habit.Name,
        iconResource: Habit.IconResource,
        countability: Habit.Countability
    ): Habit = withContext(Dispatchers.IO) {
        val habit = Habit(
            id = Habit.Id(idGenerator.nextId()),
            name = name,
            iconResource = iconResource,
            countability = countability
        )
        habitsDao.insert(habit.toDatabaseHabit())
        habit
    }

    suspend fun updateHabit(
        id: Habit.Id,
        name: Habit.Name,
        iconResource: Habit.IconResource
    ) = withContext(Dispatchers.IO) {
        habitsDao.update(
            id = id.value,
            name = name.value,
            iconId = iconResource.iconId
        )
    }

    suspend fun deleteHabit(id: Habit.Id) = withContext(Dispatchers.IO) {
        habitsDao.deleteById(id.value)
    }

    fun habitIdsFlow() =
        habitsDao.entityIdsFlow().mapItems { Habit.Id(it) }

    fun habitFlowById(id: Habit.Id) =
        habitsDao.entityFlowById(id.value).map { it?.toHabit() }

    suspend fun getHabitById(id: Habit.Id) = withContext(Dispatchers.IO) {
        habitsDao.getEntityById(id.value)?.toHabit()
    }

    suspend fun habitNameExists(name: Habit.Name) = withContext(Dispatchers.IO) {
        habitsDao.countEntitiesByName(name.value) > 0
    }

    private fun DatabaseHabit.toHabit() = Habit(
        Habit.Id(id), Habit.Name(name), Habit.IconResource(iconId), Habit.Countability(isCountable)
    )

    private fun Habit.toDatabaseHabit() = DatabaseHabit(
        id.value, name.value, iconResource.iconId, countability.idCountable
    )
}