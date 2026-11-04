package epicarchitect.breakbadhabits.database

import androidx.room3.ColumnTypeConverter
import kotlin.uuid.Uuid

class UuidConverter {
    @ColumnTypeConverter
    fun uuidToString(value: Uuid) = value.toString()

    @ColumnTypeConverter
    fun stringToUuid(value: String) = Uuid.parse(value)
}
