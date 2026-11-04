package epicarchitect.breakbadhabits.database

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlin.time.Instant

@Entity(tableName = "premium_offer_info")
data class PremiumOfferInfo(
    @PrimaryKey
    @ColumnInfo("id")
    val id: String,
    @ColumnInfo("dismissTime")
    val dismissTime: Instant
)
