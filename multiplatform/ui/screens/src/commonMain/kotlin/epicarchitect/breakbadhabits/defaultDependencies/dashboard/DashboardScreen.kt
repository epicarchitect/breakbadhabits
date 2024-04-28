package epicarchitect.breakbadhabits.defaultDependencies.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.features.HabitIcons
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.features.dashboard.Dashboard
import epicarchitect.breakbadhabits.features.dashboard.DashboardDependencies

class DashboardScreen : Screen {
    @Composable
    override fun Content() {
        Dashboard(
            DashboardDependencies(
                resources = LocalizedDashboardResources(Locale.current),
                navigation = DefaultDashboardNavigation(LocalNavigator.currentOrThrow),
                mainDatabase = LocalAppModule.current.mainDatabase,
                habitIcons = HabitIcons
            )
        )
    }
}