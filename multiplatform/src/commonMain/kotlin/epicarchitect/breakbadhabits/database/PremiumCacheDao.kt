package epicarchitect.breakbadhabits.database

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert

@Dao
interface PremiumCacheDao {
    @Query("SELECT * FROM premium_cache WHERE id = 0")
    suspend fun get(): PremiumCache?

    @Upsert
    suspend fun save(cache: PremiumCache)

    @Query("DELETE FROM premium_cache")
    suspend fun clear()
}
