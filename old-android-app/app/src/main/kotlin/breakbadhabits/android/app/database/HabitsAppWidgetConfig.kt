package breakbadhabits.android.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habitsAppWidgetConfigs")
data class HabitsAppWidgetConfig(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "appWidgetId")
    val appWidgetId: Int,

    @ColumnInfo(name = "habitIdsJson")
    val habitIdsJson: String
)