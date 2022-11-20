package breakbadhabits.android.app.database.v3

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habitEvents")
data class HabitEventEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "habitId")
    val habitId: Int,

    @ColumnInfo(name = "time")
    val time: Long,

    @ColumnInfo(name = "comment")
    val comment: String?
)