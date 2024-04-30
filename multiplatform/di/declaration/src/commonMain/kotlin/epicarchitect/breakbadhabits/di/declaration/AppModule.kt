package epicarchitect.breakbadhabits.di.declaration

import epicarchitect.breakbadhabits.sqldelight.main.MainDatabase

interface AppModule {
//    val habits: HabitsLogicModule
    val format: FormatModule
//    val dateTime: DateTimeLogicModule
    val coroutines: CoroutinesModule
//    val identification: IdentificationModule
    val mainDatabase: MainDatabase
}