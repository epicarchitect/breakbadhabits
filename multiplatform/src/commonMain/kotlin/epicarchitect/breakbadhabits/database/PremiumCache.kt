package epicarchitect.breakbadhabits.database

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "premium_cache")
data class PremiumCache(
    @ColumnInfo("isPremium")
    val isPremium: Boolean = false,
    @ColumnInfo("source")
    val source: String? = null,
    @ColumnInfo("expiresAtEpochMs")
    val expiresAtEpochMs: Long? = null
) {
    @PrimaryKey
    @ColumnInfo("id")
    var id: Int = 0
}
