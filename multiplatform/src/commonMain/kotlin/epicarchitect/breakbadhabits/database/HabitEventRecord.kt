package epicarchitect.breakbadhabits.database

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    tableName = "habit_event_records",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["id"],
            childColumns = ["habitId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("habitId", "endTime")]
)
data class HabitEventRecord(
    @PrimaryKey
    @ColumnInfo("id")
    val id: Uuid,
    @ColumnInfo("habitId")
    val habitId: Uuid,
    @ColumnInfo("startTime")
    val startTime: Instant,
    @ColumnInfo("endTime")
    val endTime: Instant,
    @ColumnInfo("eventCount")
    val eventCount: Int,
    @ColumnInfo("comment")
    val comment: String
)
