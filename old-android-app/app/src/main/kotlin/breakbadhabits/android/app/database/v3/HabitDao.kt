package breakbadhabits.android.app.database.v3

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert
    suspend fun insertEntity(entity: HabitEntity)

    @Update
    suspend fun updateEntity(entity: HabitEntity)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteEntityById(id: Int)

    @Query("SELECT * FROM habits")
    fun entityListFlow(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id in (:ids)")
    suspend fun entityListByIds(ids: List<Int>): List<HabitEntity>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun entityByIdFlow(id: Int): Flow<HabitEntity?>

    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun entityById(id: Int): HabitEntity?

    @Query("SELECT count(*) FROM habits WHERE name = :name")
    suspend fun countEntitiesByName(name: String): Int

}