package epicarchitect.breakbadhabits.database

import com.russhwolf.settings.Settings

object AppData {
    val idGenerator = IdGenerator(settings = Settings())
    val mainDatabase = MainDatabase(databaseName = "breakbadhabits-main.db")
}