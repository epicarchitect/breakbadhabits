package epicarchitect.breakbadhabits.database

import androidx.room3.Dao
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
interface HabitsDao {
    @Query("SELECT * FROM habits")
    fun habits(): Flow<List<Habit>>

    @Query(
        "SELECT h.id, h.name, h.iconId, (" +
                "SELECT MAX(r.endTime) FROM habit_event_records r WHERE r.habitId = h.id" +
                ") AS lastEventEndTime FROM habits h"
    )
    fun habitsWithLastEvent(): Flow<List<HabitWithLastEvent>>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun habitById(id: Uuid): Flow<Habit?>

    @Query("SELECT count(*) FROM habits")
    suspend fun count(): Long

    @Query("SELECT count(*) FROM habits WHERE name = :name")
    suspend fun countWithName(name: String): Long

    @Query(
        "INSERT INTO habits (id, name, iconId) " +
                "VALUES (:id, :name, :iconId)"
    )
    suspend fun insert(
        id: Uuid,
        name: String,
        iconId: Int
    )

    @Query(
        "UPDATE habits SET name = :name, iconId = :iconId WHERE id = :id"
    )
    suspend fun update(
        id: Uuid,
        name: String,
        iconId: Int
    )


    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteById(id: Uuid)

    @Query("DELETE FROM habits")
    suspend fun deleteAll()
}
