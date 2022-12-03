package breakbadhabits.android.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitTracksDao {

    @Insert
    suspend fun insert(entity: HabitTrack)

    @Query("UPDATE habitTracks SET startDay = :startDay, endDay = :endDay, dailyCount = :dailyCount, comment = :comment  WHERE id = :id")
    suspend fun update(id: Int, startDay: String, endDay: String, dailyCount: Double, comment: String?)

    @Query("DELETE FROM habitTracks WHERE id = :id")
    suspend fun deleteEntityById(id: Int)

    @Query("DELETE FROM habitTracks WHERE habitId = :habitId")
    suspend fun deleteEntitiesByHabitId(habitId: Int)

    @Query("SELECT * FROM habitTracks WHERE id = :id")
    fun entityByIdFlow(id: Int): Flow<HabitTrack?>

    @Query("SELECT * FROM habitTracks WHERE id = :id")
    suspend fun getEntityById(id: Int): HabitTrack?

    @Query("SELECT * FROM habitTracks")
    fun entityListFlow(): Flow<List<HabitTrack>>

    @Query("SELECT * FROM habitTracks WHERE habitId = :habitId")
    fun entityListByHabitIdFlow(habitId: Int): Flow<List<HabitTrack>>

}
