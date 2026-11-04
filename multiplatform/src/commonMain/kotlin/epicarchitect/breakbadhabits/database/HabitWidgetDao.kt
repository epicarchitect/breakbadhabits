package epicarchitect.breakbadhabits.database

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitWidgetDao {
    @Query("SELECT * FROM habit_widgets")
    fun habitWidgets(): Flow<List<HabitWidget>>

    @Query("SELECT * FROM habit_widgets WHERE systemId = :systemId")
    suspend fun widgetBySystemId(systemId: Int): HabitWidget?

    @Upsert
    suspend fun save(widget: HabitWidget)

    @Query("DELETE FROM habit_widgets WHERE systemId = :systemId")
    suspend fun deleteBySystemId(systemId: Int)

    @Query("DELETE FROM habit_widgets")
    suspend fun deleteAll()
}
