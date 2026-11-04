package epicarchitect.breakbadhabits.database

interface AppDatabaseFactory {
    fun create(databaseName: String): AppDatabase
}
