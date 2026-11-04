package epicarchitect.breakbadhabits.database

import androidx.room3.ColumnTypeConverter
import kotlinx.serialization.json.Json

class HabitOrderConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @ColumnTypeConverter
    fun fromHabitOrder(value: List<String>): String =
        json.encodeToString(value)

    @ColumnTypeConverter
    fun toHabitOrder(value: String): List<String> =
        runCatching { json.decodeFromString<List<String>>(value) }
            .getOrDefault(emptyList())
}
