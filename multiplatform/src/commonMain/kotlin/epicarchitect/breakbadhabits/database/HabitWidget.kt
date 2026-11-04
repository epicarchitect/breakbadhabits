package epicarchitect.breakbadhabits.database

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey
import kotlin.uuid.Uuid

@Entity(
    tableName = "habit_widgets",
    indices = [Index(value = ["systemId"], unique = true)]
)
data class HabitWidget(
    @ColumnInfo("systemId")
    val systemId: Int,
    @ColumnInfo("habitIds")
    val habitIds: List<Uuid>
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id: Int = 0
}
