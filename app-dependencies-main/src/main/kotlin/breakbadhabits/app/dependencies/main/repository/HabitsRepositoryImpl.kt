package breakbadhabits.app.dependencies.main.repository

import breakbadhabits.app.dependencies.main.database.HabitsDao
import breakbadhabits.entity.Habit
import breakbadhabits.extension.coroutines.flow.mapItems
import breakbadhabits.feature.habits.model.HabitsRepository
import kolmachikhin.alexander.validation.Correct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import breakbadhabits.app.dependencies.main.database.Habit as DatabaseHabit

internal class HabitsRepositoryImpl(
    private val idGenerator: IdGenerator,
    private val habitsDao: HabitsDao
) : HabitsRepository {

    override suspend fun insertHabit(
        name: Correct<Habit.Name>,
        iconResource: Habit.IconResource,
        countability: Habit.Countability
    ): Habit = withContext(Dispatchers.IO) {
        val habit = Habit(
            id = Habit.Id(idGenerator.nextId()),
            name = name.data,
            iconResource = iconResource,
            countability = countability
        )
        habitsDao.insert(habit.toDatabaseHabit())
        habit
    }

    override suspend fun updateHabit(
        id: Habit.Id,
        name: Correct<Habit.Name>,
        iconResource: Habit.IconResource
    ) = withContext(Dispatchers.IO) {
        habitsDao.update(
            id = id.value,
            name = name.data.value,
            iconId = iconResource.iconId
        )
    }

    override suspend fun deleteHabit(id: Habit.Id) = withContext(Dispatchers.IO) {
        habitsDao.deleteById(id.value)
    }

    override fun habitIdsFlow() =
        habitsDao.entityIdsFlow().mapItems { Habit.Id(it) }

    override fun habitFlowById(id: Habit.Id) =
        habitsDao.entityFlowById(id.value).map { it?.toHabit() }

    override suspend fun getHabitById(id: Habit.Id) = withContext(Dispatchers.IO) {
        habitsDao.getEntityById(id.value)?.toHabit()
    }

    override suspend fun habitNameExists(name: Habit.Name) = withContext(Dispatchers.IO) {
        habitsDao.countEntitiesByName(name.value) > 0
    }

    private fun DatabaseHabit.toHabit() = Habit(
        Habit.Id(id), Habit.Name(name), Habit.IconResource(iconId), Habit.Countability(isCountable)
    )

    private fun Habit.toDatabaseHabit() = DatabaseHabit(
        id.value, name.value, iconResource.iconId, countability.idCountable
    )
}