package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.screens.dashboard.Dashboard
import epicarchitect.breakbadhabits.screens.dashboard.LocalDashboardResources
import epicarchitect.breakbadhabits.screens.dashboardResourcesOf
import epicarchitect.breakbadhabits.screens.hold

class DashboardScreen : Screen {
    @Composable
    override fun Content() {
        CompositionLocalProvider(
            LocalDashboardResources provides dashboardResourcesOf(Locale.current)
        ) {
            val navigator = LocalNavigator.currentOrThrow
            val presentationModule = LocalAppModule.current.presentation
            val viewModel = hold(factory = presentationModule.dashboard::dashboardViewModel)

            Dashboard(
                habitItemsController = viewModel.itemsLoadingController,
                onHabitClick = { habitId ->
                    navigator += HabitDetailsScreen(habitId)
                },
                onAddTrackClick = { habitId ->
                    navigator += HabitTrackCreationScreen(habitId)
                },
                onHabitCreationClick = {
                    navigator += HabitCreationScreen()
                },
                onAppSettingsClick = {
                    navigator += AppSettingsScreen()
                }
            )
        }
    }
}