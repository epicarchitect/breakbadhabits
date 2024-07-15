package epicarchitect.breakbadhabits.environment.database

import app.cash.sqldelight.ColumnAdapter

object ListOfIntAdapter : ColumnAdapter<List<Int>, String> {
    override fun decode(databaseValue: String): List<Int> {
        return databaseValue.split(',').map(String::toInt)
    }

    override fun encode(value: List<Int>): String {
        return value.joinToString(separator = ",")
    }
}