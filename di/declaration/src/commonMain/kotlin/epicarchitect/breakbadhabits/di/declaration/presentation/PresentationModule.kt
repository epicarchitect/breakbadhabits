package epicarchitect.breakbadhabits.di.declaration.presentation

import epicarchitect.breakbadhabits.di.declaration.logic.LogicModule

interface PresentationModule {
    val logic: LogicModule
    val dashboard: DashboardPresentationModule
    val habits: HabitsPresentationModule
}