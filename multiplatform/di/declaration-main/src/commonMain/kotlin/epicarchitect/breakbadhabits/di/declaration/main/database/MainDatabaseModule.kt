package epicarchitect.breakbadhabits.di.declaration.main.database

import epicarchitect.breakbadhabits.database.main.MainDatabaseFactory
import epicarchitect.breakbadhabits.foundation.sqldelight.SqlDriverFactory
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class MainDatabaseModule(
    externals: MainDatabaseModuleExternals
) {
    private val mainDatabaseFactory = MainDatabaseFactory(externals.sqlDriverFactory)
    val mainDatabase: MainDatabase by lazy(mainDatabaseFactory::create)
}

interface MainDatabaseModuleExternals {
    val sqlDriverFactory: SqlDriverFactory
}