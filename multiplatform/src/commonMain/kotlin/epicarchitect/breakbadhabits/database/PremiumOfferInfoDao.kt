package epicarchitect.breakbadhabits.database

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PremiumOfferInfoDao {
    @Query("SELECT * FROM premium_offer_info")
    fun all(): Flow<List<PremiumOfferInfo>>

    @Upsert
    suspend fun save(info: PremiumOfferInfo)
}
