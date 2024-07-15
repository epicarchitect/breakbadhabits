package epicarchitect.breakbadhabits.ui.screen.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.environment.Environment
import epicarchitect.breakbadhabits.ui.component.FlowStateContainer
import epicarchitect.breakbadhabits.ui.component.stateOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme
import epicarchitect.breakbadhabits.ui.screen.appDashboard.AppDashboardScreen

@Composable
fun App() {
    FlowStateContainer(
        state = stateOfOneOrNull { Environment.database.appSettingsQueries.settings() }
    ) { settings ->
        AppTheme(
            colorScheme = AppColorsSchemes.byAppSettings(settings!!)
        ) {
            Navigator(AppDashboardScreen())
        }
    }
}