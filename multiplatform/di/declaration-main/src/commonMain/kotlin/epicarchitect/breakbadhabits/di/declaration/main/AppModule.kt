package epicarchitect.breakbadhabits.di.declaration.main

import epicarchitect.breakbadhabits.di.declaration.AppModule
import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

class AppModule(
    formatExternals: FormatModuleExternals,
    mainDatabaseExternals: MainDatabaseModuleExternals,
//    habitsLogicExternals: HabitsLogicModuleExternals
) : AppModule {
//    private val mainDatabaseModule = MainDatabaseModule(mainDatabaseExternals)
    override val coroutines = CoroutinesModule()
    override val format = FormatModule(formatExternals)
//    override val dateTime = DateTimeLogicModule(coroutines)
//    override val identification = IdentificationModule()
//    override val habits = HabitsLogicModule(
//        coroutines,
//        dateTime,
//        identification,
//        mainDatabaseModule,
//        habitsLogicExternals
//    )
    override val mainDatabase: MainDatabase = mainDatabaseModule.mainDatabase
}