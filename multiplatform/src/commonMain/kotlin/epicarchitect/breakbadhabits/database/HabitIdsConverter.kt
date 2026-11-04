package epicarchitect.breakbadhabits.database

import androidx.room3.ColumnTypeConverter
import kotlin.uuid.Uuid

class HabitIdsConverter {
    @ColumnTypeConverter
    fun habitIdsToString(value: List<Uuid>) = value.joinToString(separator = ",")

    @ColumnTypeConverter
    fun stringToHabitIds(value: String) =
        if (value.isEmpty()) emptyList()
        else value.split(",").map { Uuid.parse(it) }
}
