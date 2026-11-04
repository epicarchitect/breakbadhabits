package epicarchitect.breakbadhabits.database

import androidx.room3.Dao
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface HabitEventRecordsDao {
    @Query("SELECT * FROM habit_event_records")
    suspend fun records(): List<HabitEventRecord>

    @Query("SELECT * FROM habit_event_records WHERE habitId = :habitId")
    fun recordsByHabitId(habitId: Uuid): Flow<List<HabitEventRecord>>

    @Query("SELECT * FROM habit_event_records WHERE id = :id")
    fun recordById(id: Uuid): Flow<HabitEventRecord?>

    @Query(
        "SELECT * FROM habit_event_records WHERE habitId = :habitId " +
                "AND endTime = (SELECT max(endTime) FROM habit_event_records WHERE habitId = :habitId) LIMIT 1"
    )
    fun recordByHabitIdAndMaxEndTime(habitId: Uuid): Flow<HabitEventRecord?>

    @Query(
        "INSERT INTO habit_event_records (id, habitId, startTime, endTime, eventCount, comment) " +
                "VALUES (:id, :habitId, :startTime, :endTime, :eventCount, :comment)"
    )
    suspend fun insert(
        id: Uuid,
        habitId: Uuid,
        startTime: Instant,
        endTime: Instant,
        eventCount: Int,
        comment: String
    )

    @Query(
        "UPDATE habit_event_records SET startTime = :startTime, endTime = :endTime, " +
                "eventCount = :eventCount, comment = :comment WHERE id = :id"
    )
    suspend fun update(
        id: Uuid,
        startTime: Instant,
        endTime: Instant,
        eventCount: Int,
        comment: String
    )

    @Query("DELETE FROM habit_event_records WHERE id = :id")
    suspend fun deleteById(id: Uuid)
}
