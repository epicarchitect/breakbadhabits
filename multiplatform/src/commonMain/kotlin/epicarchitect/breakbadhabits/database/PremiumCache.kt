package epicarchitect.breakbadhabits.database

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlin.time.Instant

@Entity(tableName = "premium_cache")
data class PremiumCache(
    @ColumnInfo("source")
    val source: String,
    @ColumnInfo("expiresAt")
    val expiresAt: Instant,
    @ColumnInfo("productId")
    val productId: String? = null,
    @ColumnInfo("amountLabel")
    val amountLabel: String? = null,
    @ColumnInfo("priceMinorUnits")
    val priceMinorUnits: Int? = null,
    @ColumnInfo("discountMinorUnits")
    val discountMinorUnits: Int? = null,
    @ColumnInfo("currency")
    val currency: String? = null,
    @ColumnInfo("sandbox")
    val sandbox: Boolean = false,
    @ColumnInfo("acknowledgementState")
    val acknowledgementState: String? = null
) {
    @PrimaryKey
    @ColumnInfo("id")
    var id: Int = 0
}

val PremiumLifetimeExpiresAt = Instant.fromEpochSeconds(253402300799)
