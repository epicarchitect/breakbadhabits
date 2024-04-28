package epicarchitect.breakbadhabits.di.declaration.main

import epicarchitect.breakbadhabits.database.main.MainDatabaseFactory
import epicarchitect.breakbadhabits.foundation.sqldelight.SqlDriverFactory
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class MainDatabaseModule(
    externals: MainDatabaseModuleExternals
) {
    val mainDatabase = MainDatabaseFactory(externals.sqlDriverFactory).create("breakbadhabits-main.db")
}

interface MainDatabaseModuleExternals {
    val sqlDriverFactory: SqlDriverFactory
}