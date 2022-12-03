package breakbadhabits.android.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {

    @Insert
    suspend fun insert(entity: Habit)

    @Query("UPDATE habits SET name = :name, iconId = :iconId WHERE id = :id")
    suspend fun update(id: Int, name: String, iconId: Int)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT id FROM habits")
    fun entityIdsFlow(): Flow<List<Int>>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun entityFlowById(id: Int): Flow<Habit?>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun getEntityById(id: Int): Habit?

    @Query("SELECT count(*) FROM habits WHERE name = :name")
    suspend fun countEntitiesByName(name: String): Int

}