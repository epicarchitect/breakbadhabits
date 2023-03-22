package breakbadhabits.app.database

import app.cash.sqldelight.ColumnAdapter
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

internal object ListOfLongsAdapter : ColumnAdapter<List<Long>, String> {
    override fun decode(databaseValue: String): List<Long> {
        return Json.decodeFromString(serializer(), databaseValue)
    }

    override fun encode(value: List<Long>): String {
        return Json.encodeToString(serializer(), value)
    }
}