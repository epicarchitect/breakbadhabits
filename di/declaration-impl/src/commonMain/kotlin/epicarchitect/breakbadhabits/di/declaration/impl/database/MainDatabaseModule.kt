package epicarchitect.breakbadhabits.di.declaration.impl.database

import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class MainDatabaseModule(
    external: MainDatabaseModuleExternal
) : MainDatabaseModuleExternal by external

interface MainDatabaseModuleExternal {
    val mainDatabase: MainDatabase
}
