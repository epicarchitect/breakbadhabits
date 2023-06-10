package epicarchitect.breakbadhabits.di.declaration.main.database

import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class MainDatabaseModule(
    externals: MainDatabaseModuleExternals
) : MainDatabaseModuleExternals by externals

interface MainDatabaseModuleExternals {
    val mainDatabase: MainDatabase
}
