package epicarchitect.breakbadhabits.database

import com.russhwolf.settings.Settings
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

object AppData {
    val idGenerator = IdGenerator(settings = Settings())
    val mainDatabase = MainDatabase(databaseName = "breakbadhabits-main.db")
}