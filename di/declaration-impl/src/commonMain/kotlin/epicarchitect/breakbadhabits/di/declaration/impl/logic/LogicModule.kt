package epicarchitect.breakbadhabits.di.declaration.impl.logic

import epicarchitect.breakbadhabits.di.declaration.foundation.FoundationModule
import epicarchitect.breakbadhabits.di.declaration.impl.database.MainDatabaseModule
import epicarchitect.breakbadhabits.di.declaration.impl.database.MainDatabaseModuleExternal
import epicarchitect.breakbadhabits.di.declaration.impl.foundation.DateTimeLogicModuleImpl
import epicarchitect.breakbadhabits.di.declaration.logic.LogicModule

class LogicModuleImpl(
    override val foundation: FoundationModule,
    external: LogicModuleExternal
) : LogicModule, LogicModuleExternal by external {
    private val mainDatabase by lazy {
        MainDatabaseModule(
            external = mainDatabaseExternal
        )
    }
    override val dateTime by lazy {
        DateTimeLogicModuleImpl(
            coroutinesModule = foundation.coroutines
        )
    }
    override val habits by lazy {
        HabitsLogicModuleImpl(
            coroutinesModule = foundation.coroutines,
            dateTimeLogicModule = dateTime,
            identificationModule = foundation.identification,
            mainDatabaseModule = mainDatabase,
            external = habitsExternal
        )
    }
}

interface LogicModuleExternal {
    val mainDatabaseExternal: MainDatabaseModuleExternal
    val habitsExternal: HabitsLogicModuleExternal
}
