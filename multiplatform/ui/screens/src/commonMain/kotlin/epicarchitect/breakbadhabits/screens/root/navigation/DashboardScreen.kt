package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class DashboardScreen : Screen {
    @Composable
    override fun Content() {
//        CompositionLocalProvider(
//            LocalDashboardResources provides dashboardResourcesOf(Locale.current)
//        ) {
//            val navigator = LocalNavigator.currentOrThrow
//            val presentationModule = LocalAppModule.current.presentation
//            val viewModel = hold(factory = presentationModule.dashboard::dashboardViewModel)
//
//            Dashboard(
//                viewModel = viewModel,
//                onHabitClick = { habitId ->
//                    navigator += HabitDetailsScreen(habitId)
//                },
//                onAddTrackClick = { habitId ->
//                    navigator += HabitTrackCreationScreen(habitId)
//                },
//                onHabitCreationClick = {
//                    navigator += HabitCreationScreen()
//                },
//                onAppSettingsClick = {
//                    navigator += AppSettingsScreen()
//                }
//            )
//        }
    }
}