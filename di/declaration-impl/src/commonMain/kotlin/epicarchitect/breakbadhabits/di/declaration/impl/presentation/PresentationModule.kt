package epicarchitect.breakbadhabits.di.declaration.impl.presentation

import epicarchitect.breakbadhabits.di.declaration.logic.LogicModule
import epicarchitect.breakbadhabits.di.declaration.presentation.PresentationModule

class PresentationModuleImpl(
    override val logic: LogicModule
) : PresentationModule {
    override val dashboard by lazy {
        DashboardPresentationModuleImpl(
            habitsLogicModule = logic.habits
        )
    }
    override val habits by lazy {
        HabitsPresentationModuleImpl(
            habitsLogicModule = logic.habits,
            dateTimeLogicModule = logic.dateTime
        )
    }
}