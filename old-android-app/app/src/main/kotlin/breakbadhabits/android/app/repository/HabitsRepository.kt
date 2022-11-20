package breakbadhabits.android.app.repository

import breakbadhabits.entity.Habit
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface HabitsRepository {
    suspend fun createHabit(
        habitName: String,
        habitIconId: Int,
        isCountable: Boolean,
        firstEventDay: LocalDate,
        lastEventDay: LocalDate,
        averageDayValue: Int
    )

    suspend fun deleteHabit(id: Int)

    suspend fun saveHabit(habit: Habit)

    suspend fun habitById(habitId: Int): Habit?

    fun habitsFlow(): Flow<List<Habit>>

    suspend fun habitsByIds(ids: List<Int>): List<Habit>

    fun habitByIdFlow(id: Int): Flow<Habit?>

    suspend fun habitNameExists(habitName: String): Boolean

    suspend fun generateNextId(): Int
}