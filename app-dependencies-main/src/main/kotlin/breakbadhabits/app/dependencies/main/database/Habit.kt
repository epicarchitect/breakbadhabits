package breakbadhabits.app.dependencies.main.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
internal data class Habit(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "iconId")
    val iconId: Int,

    @ColumnInfo(name = "isCountable")
    val isCountable: Boolean
)