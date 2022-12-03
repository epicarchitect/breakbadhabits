package breakbadhabits.android.app.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habitTracks")
data class HabitTrack(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "habitId")
    val habitId: Int,

    @ColumnInfo(name = "startDay")
    val startDay: String,

    @ColumnInfo(name = "endDay")
    val endDay: String,

    @ColumnInfo(name = "dailyCount")
    val dailyCount: Double,

    @ColumnInfo(name = "comment")
    val comment: String?,
)