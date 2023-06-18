package epicarchitect.breakbadhabits.di.declaration.main.presentation

import epicarchitect.breakbadhabits.di.declaration.logic.HabitsLogicModule
import epicarchitect.breakbadhabits.di.declaration.presentation.DashboardPresentationModule
import epicarchitect.breakbadhabits.presentation.dashboard.DashboardViewModel

class DashboardPresentationModule(
    private val habitsLogicModule: HabitsLogicModule
) : DashboardPresentationModule {

    override val dashboardViewModel
        get() = DashboardViewModel(
            habitProvider = habitsLogicModule.habitProvider,
            habitAbstinenceProvider = habitsLogicModule.habitAbstinenceProvider
        )
}
