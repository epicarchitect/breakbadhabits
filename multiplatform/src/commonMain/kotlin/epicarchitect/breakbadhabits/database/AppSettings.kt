package epicarchitect.breakbadhabits.database

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "app_settings")
data class AppSettings(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Int,
    @ColumnInfo("habitOrder")
    val habitOrder: List<String>
)
