package breakbadhabits.android.app.database.v3

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsAppWidgetConfigDao {

    @Insert
    suspend fun insertEntity(entity: HabitsAppWidgetConfigEntity)

    @Update
    suspend fun updateEntity(entity: HabitsAppWidgetConfigEntity)

    @Query("UPDATE habitsAppWidgetConfigs SET habitIdsJson = :habitIdsJson, title = :title  WHERE id = :id ")
    suspend fun updateEntity(id: Int, title: String, habitIdsJson: String)

    @Query("DELETE FROM habitsAppWidgetConfigs WHERE id = :id")
    suspend fun deleteEntityById(id: Int)

    @Query("DELETE FROM habitsAppWidgetConfigs WHERE appWidgetId IN (:appWidgetIds)")
    suspend fun deleteEntityByAppWidgetIds(appWidgetIds: List<Int>)

    @Query("SELECT * FROM habitsAppWidgetConfigs")
    fun entityListFlow(): Flow<List<HabitsAppWidgetConfigEntity>>

    @Query("SELECT * FROM habitsAppWidgetConfigs WHERE id = :id")
    fun entityByIdFlow(id: Int): Flow<HabitsAppWidgetConfigEntity?>

    @Query("SELECT * FROM habitsAppWidgetConfigs WHERE id = :id")
    suspend fun entityById(id: Int): HabitsAppWidgetConfigEntity?

    @Query("SELECT * FROM habitsAppWidgetConfigs WHERE appWidgetId = :appWidgetId")
    suspend fun entityByAppWidgetId(appWidgetId: Int): HabitsAppWidgetConfigEntity?
}