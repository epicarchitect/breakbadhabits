package epicarchitect.breakbadhabits.di.declaration.main.logic

import epicarchitect.breakbadhabits.di.declaration.foundation.FoundationModule
import epicarchitect.breakbadhabits.di.declaration.logic.LogicModule
import epicarchitect.breakbadhabits.di.declaration.main.database.MainDatabaseModule
import epicarchitect.breakbadhabits.di.declaration.main.database.MainDatabaseModuleExternals
import epicarchitect.breakbadhabits.di.declaration.main.foundation.DateTimeLogicModule

class LogicModule(
    override val foundation: FoundationModule,
    externals: LogicModuleExternals
) : LogicModule, LogicModuleExternals by externals {
    private val mainDatabase by lazy {
        MainDatabaseModule(
            externals = mainDatabaseExternal
        )
    }
    override val dateTime by lazy {
        DateTimeLogicModule(
            coroutinesModule = foundation.coroutines
        )
    }
    override val habits by lazy {
        HabitsLogicModule(
            coroutinesModule = foundation.coroutines,
            dateTimeLogicModule = dateTime,
            identificationModule = foundation.identification,
            mainDatabaseModule = mainDatabase,
            externals = habitsExternal
        )
    }
}

interface LogicModuleExternals {
    val mainDatabaseExternal: MainDatabaseModuleExternals
    val habitsExternal: HabitsLogicModuleExternals
}