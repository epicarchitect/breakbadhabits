package breakbadhabits.android.app.repository

import breakbadhabits.android.app.data.Habit
import breakbadhabits.android.app.data.HabitEvent
import breakbadhabits.android.app.database.HabitEntity
import breakbadhabits.android.app.database.HabitEventEntity
import breakbadhabits.android.app.database.MainDatabase
import breakbadhabits.coroutines.flow.mapItems
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HabitsRepository(
    private val idGenerator: IdGenerator,
    mainDatabase: MainDatabase
) {

    private val habitDao = mainDatabase.habitDao
    private val habitEventDao = mainDatabase.habitEventDao

    suspend fun createHabitEvent(
        habitId: Int,
        timeInMillis: Long,
        comment: String?
    ) = habitEventDao.insertEntity(
        HabitEventEntity(
            idGenerator.nextId(),
            habitId,
            timeInMillis,
            comment
        )
    )

    suspend fun updateHabitEvent(
        habitEventId: Int,
        timeInMillis: Long,
        comment: String?
    ) = habitEventDao.updateEntity(
        habitEventId,
        timeInMillis,
        comment
    )

    suspend fun deleteHabitEventsByHabitId(habitId: Int) = habitEventDao.deleteEntitiesByHabitId(habitId)

    suspend fun deleteHabitEventById(habitEventId: Int) = habitEventDao.deleteEntityById(habitEventId)

    fun habitEventByIdFlow(habitEventId: Int) = habitEventDao.entityByIdFlow(habitEventId).map { it?.toHabitEvent() }

    fun habitEventListByHabitIdFlow(habitId: Int) = habitEventDao.entityListByHabitIdFlow(habitId).mapItems { it.toHabitEvent() }

    fun lastByTimeHabitEventByHabitIdFlow(habitId: Int) = habitEventDao.lastByTimeEntityByHabitIdFlow(habitId).map { it?.toHabitEvent() }

    fun habitEventListFlow() = habitEventDao.entityListFlow().mapItems { it.toHabitEvent() }

    suspend fun lastByTimeHabitEventByHabitId(habitId: Int) = habitEventDao.lastByTimeEntityByHabitId(habitId)?.toHabitEvent()

    suspend fun createHabit(
        habitName: String,
        habitIconId: Int,
        lastEventTime: Long
    ) = coroutineScope {
        val habitId = idGenerator.nextId()
        val habitEventId = idGenerator.nextId()

        launch {
            habitDao.insertEntity(
                HabitEntity(
                    habitId,
                    habitName,
                    habitIconId
                )
            )
        }

        launch {
            habitEventDao.insertEntity(
                HabitEventEntity(
                    habitEventId,
                    habitId,
                    lastEventTime,
                    null
                )
            )
        }
    }

    suspend fun deleteHabit(id: Int) = coroutineScope {
        launch { habitDao.deleteEntityById(id) }
        launch { deleteHabitEventsByHabitId(id) }
    }

    suspend fun updateHabit(
        habitId: Int,
        habitName: String,
        iconId: Int
    ) = habitDao.updateEntity(
        HabitEntity(
            habitId,
            habitName,
            iconId
        )
    )

    suspend fun habitById(habitId: Int) = habitDao.entityById(habitId)?.toHabit()

    fun habitListFlow() = habitDao.entityListFlow().mapItems { it.toHabit() }

    suspend fun habitListByIds(ids: List<Int>) = habitDao.entityListByIds(ids).map { it.toHabit() }

    fun habitByIdFlow(id: Int) = habitDao.entityByIdFlow(id).map { it?.toHabit() }

    suspend fun habitNameExists(habitName: String) = habitDao.countEntitiesByName(habitName) > 0

    private fun HabitEntity.toHabit() = Habit(
        id, name, iconId
    )

    private fun HabitEventEntity.toHabitEvent() = HabitEvent(
        id, habitId, time, comment
    )
}
