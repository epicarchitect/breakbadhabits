package breakbadhabits.android.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitTracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEntity(entity: HabitTrack)

    @Query("DELETE FROM habitTracks WHERE id = :id")
    suspend fun deleteEntityById(id: Int)

    @Query("DELETE FROM habitTracks WHERE habitId = :habitId")
    suspend fun deleteEntitiesByHabitId(habitId: Int)

    @Query("SELECT * FROM habitTracks WHERE id = :id")
    fun entityByIdFlow(id: Int): Flow<HabitTrack?>

    @Query("SELECT * FROM habitTracks")
    fun entityListFlow(): Flow<List<HabitTrack>>

    @Query("SELECT * FROM habitTracks WHERE habitId = :habitId")
    fun entityListByHabitIdFlow(habitId: Int): Flow<List<HabitTrack>>

}
