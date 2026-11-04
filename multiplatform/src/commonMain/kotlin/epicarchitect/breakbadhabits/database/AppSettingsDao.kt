package epicarchitect.breakbadhabits.database

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AppSettingsDao {
    @Query("SELECT * FROM app_settings WHERE id = 0")
    fun appSettings(): Flow<AppSettings?>

    @Query("SELECT * FROM app_settings WHERE id = 0")
    suspend fun get(): AppSettings?

    @Upsert
    suspend fun save(settings: AppSettings)

    @Query("DELETE FROM app_settings")
    suspend fun deleteAll()
}
