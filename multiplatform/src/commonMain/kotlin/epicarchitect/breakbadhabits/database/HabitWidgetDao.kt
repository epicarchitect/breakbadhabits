package epicarchitect.breakbadhabits.database

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert

@Dao
interface HabitWidgetDao {
    @Query("SELECT * FROM habit_widgets WHERE systemId = :systemId")
    suspend fun widgetBySystemId(systemId: Int): HabitWidget?

    @Upsert
    suspend fun save(widget: HabitWidget)

    @Query("DELETE FROM habit_widgets WHERE systemId = :systemId")
    suspend fun deleteBySystemId(systemId: Int)

    @Query("DELETE FROM habit_widgets")
    suspend fun deleteAll()
}
