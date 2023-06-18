package epicarchitect.breakbadhabits.di.declaration.main.presentation

import epicarchitect.breakbadhabits.di.declaration.logic.LogicModule
import epicarchitect.breakbadhabits.di.declaration.presentation.PresentationModule

class PresentationModule(
    override val logic: LogicModule
) : PresentationModule {
    override val dashboard by lazy {
        DashboardPresentationModule(
            habitsLogicModule = logic.habits
        )
    }
    override val habits by lazy {
        HabitsPresentationModule(
            habitsLogicModule = logic.habits,
            dateTimeLogicModule = logic.dateTime
        )
    }
}
