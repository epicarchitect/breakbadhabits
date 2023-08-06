package epicarchitect.breakbadhabits.foundation.sqldelight

import app.cash.sqldelight.ColumnAdapter
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

object ListOfIntAdapter : ColumnAdapter<List<Int>, String> {
    override fun decode(databaseValue: String): List<Int> {
        return Json.decodeFromString(serializer(), databaseValue)
    }

    override fun encode(value: List<Int>): String {
        return Json.encodeToString(serializer(), value)
    }
}