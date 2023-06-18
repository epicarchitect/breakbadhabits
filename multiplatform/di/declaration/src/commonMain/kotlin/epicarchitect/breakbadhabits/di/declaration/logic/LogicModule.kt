package epicarchitect.breakbadhabits.di.declaration.logic

import epicarchitect.breakbadhabits.di.declaration.foundation.FoundationModule

interface LogicModule {
    val foundation: FoundationModule
    val dateTime: DateTimeLogicModule
    val habits: HabitsLogicModule
}
