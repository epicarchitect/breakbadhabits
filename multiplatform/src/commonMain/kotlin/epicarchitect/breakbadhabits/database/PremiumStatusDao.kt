package epicarchitect.breakbadhabits.database

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert

@Dao
interface PremiumStatusDao {
    @Query("SELECT * FROM premium_status WHERE id = 0")
    suspend fun get(): PremiumStatus?

    @Upsert
    suspend fun save(status: PremiumStatus)

    @Query("DELETE FROM premium_status")
    suspend fun clear()
}
