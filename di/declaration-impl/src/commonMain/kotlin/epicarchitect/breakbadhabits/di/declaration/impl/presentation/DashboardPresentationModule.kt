package epicarchitect.breakbadhabits.di.declaration.impl.presentation

import epicarchitect.breakbadhabits.di.declaration.logic.HabitsLogicModule
import epicarchitect.breakbadhabits.di.declaration.presentation.DashboardPresentationModule
import epicarchitect.breakbadhabits.presentation.dashboard.DashboardViewModel

class DashboardPresentationModuleImpl(
    private val habitsLogicModule: HabitsLogicModule
) : DashboardPresentationModule {
    override fun createDashboardViewModel() = DashboardViewModel(
        habitProvider = habitsLogicModule.habitProvider,
        habitAbstinenceProvider = habitsLogicModule.habitAbstinenceProvider
    )
}