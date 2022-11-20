package breakbadhabits.android.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEntity(entity: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteEntityById(id: Int)

    @Query("SELECT * FROM habits")
    fun entityListFlow(): Flow<List<Habit>>

    @Query("SELECT * FROM habits WHERE id in (:ids)")
    suspend fun entityListByIds(ids: List<Int>): List<Habit>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun entityByIdFlow(id: Int): Flow<Habit?>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun entityById(id: Int): Habit?

    @Query("SELECT count(*) FROM habits WHERE name = :name")
    suspend fun countEntitiesByName(name: String): Int

}