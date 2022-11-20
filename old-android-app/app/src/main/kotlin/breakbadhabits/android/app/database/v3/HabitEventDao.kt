package breakbadhabits.android.app.database.v3

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitEventDao {

    @Insert
    suspend fun insertEntity(entity: HabitEventEntity)

    @Query("UPDATE habitEvents SET time = :timeInMillis, comment = :comment  WHERE id = :id")
    suspend fun updateEntity(id: Int, timeInMillis: Long, comment: String?)

    @Query("DELETE FROM habitEvents WHERE id = :id")
    suspend fun deleteEntityById(id: Int)

    @Query("DELETE FROM habitEvents WHERE habitId = :habitId")
    suspend fun deleteEntitiesByHabitId(habitId: Int)

    @Query("SELECT * FROM habitEvents WHERE id = :id")
    fun entityByIdFlow(id: Int): Flow<HabitEventEntity?>

    @Query("SELECT * FROM habitEvents")
    fun entityListFlow(): Flow<List<HabitEventEntity>>

    @Query("SELECT * FROM habitEvents WHERE habitId = :habitId")
    fun entityListByHabitIdFlow(habitId: Int): Flow<List<HabitEventEntity>>

    @Query("SELECT * FROM habitEvents WHERE habitId = :habitId ORDER BY time DESC LIMIT 1")
    fun lastByTimeEntityByHabitIdFlow(habitId: Int): Flow<HabitEventEntity?>

    @Query("SELECT * FROM habitEvents WHERE habitId = :habitId ORDER BY time DESC LIMIT 1")
    suspend fun lastByTimeEntityByHabitId(habitId: Int): HabitEventEntity?

}
