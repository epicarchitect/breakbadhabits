package epicarchitect.breakbadhabits.database

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlin.uuid.Uuid

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Uuid,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("iconId")
    val iconId: Int
)
