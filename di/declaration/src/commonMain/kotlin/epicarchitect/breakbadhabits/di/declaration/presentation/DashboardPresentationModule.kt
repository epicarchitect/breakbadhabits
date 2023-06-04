package epicarchitect.breakbadhabits.di.declaration.presentation

import epicarchitect.breakbadhabits.presentation.dashboard.DashboardViewModel

interface DashboardPresentationModule {
    fun createDashboardViewModel(): DashboardViewModel
}