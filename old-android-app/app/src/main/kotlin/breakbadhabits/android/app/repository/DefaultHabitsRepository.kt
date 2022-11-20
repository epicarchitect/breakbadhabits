package breakbadhabits.android.app.repository

import breakbadhabits.android.app.database.MainDatabase
import breakbadhabits.entity.Habit
import breakbadhabits.entity.HabitTrack
import breakbadhabits.extension.coroutines.flow.mapItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import breakbadhabits.android.app.database.Habit as DatabaseHabit
import breakbadhabits.android.app.database.HabitTrack as DatabaseHabitTrack

class DefaultHabitsRepository(
    private val idGenerator: IdGenerator,
    mainDatabase: MainDatabase
) : HabitsRepository {

    private val habitDao = mainDatabase.habitDao
    private val habitTracksDao = mainDatabase.habitTracksDao

    override suspend fun createHabit(
        habitName: String,
        habitIconId: Int,
        isCountable: Boolean,
        firstEventDay: LocalDate,
        lastEventDay: LocalDate,
        averageDayValue: Int
    ) = withContext(Dispatchers.IO) {
        val habitId = idGenerator.nextId()
        val habitEventId = idGenerator.nextId()

        saveHabit(
            Habit(
                habitId,
                habitName,
                habitIconId,
                isCountable
            )
        )

        saveHabitTrack(
            HabitTrack(
                habitEventId,
                habitId,
                firstEventDay,
                lastEventDay,
                averageDayValue,
                null
            )
        )
    }

    override suspend fun deleteHabit(id: Int) = habitDao.deleteEntityById(id)

    override suspend fun saveHabit(habit: Habit) = habitDao.saveEntity(habit.toDatabaseEntity())

    override suspend fun habitById(habitId: Int) = habitDao.entityById(habitId)?.toEntity()

    override fun habitsFlow() = habitDao.entityListFlow().mapItems { it.toEntity() }

    override suspend fun habitsByIds(ids: List<Int>) = habitDao.entityListByIds(ids).map { it.toEntity() }

    override fun habitByIdFlow(id: Int) = habitDao.entityByIdFlow(id).map { it?.toEntity() }

    override suspend fun habitNameExists(habitName: String) = habitDao.countEntitiesByName(habitName) > 0

    override fun generateNextId() = idGenerator.nextId()

    private fun Habit.toDatabaseEntity() = DatabaseHabit(
        id,
        name,
        iconId,
        isCountable
    )

    private fun DatabaseHabit.toEntity() = Habit(
        id,
        name,
        iconId,
        isCountable
    )

    private fun HabitTrack.toDatabaseEntity() = DatabaseHabitTrack(
        id,
        habitId,
        startDay.toString(),
        endDay.toString(),
        dayValue,
        comment
    )

    private fun DatabaseHabitTrack.toEntity() = HabitTrack(
        id,
        habitId,
        LocalDate.parse(startDay),
        LocalDate.parse(endDay),
        dayValue,
        comment
    )
}
