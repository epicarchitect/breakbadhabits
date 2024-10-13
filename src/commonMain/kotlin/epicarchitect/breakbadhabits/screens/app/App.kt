package epicarchitect.breakbadhabits.screens.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.Environment
import epicarchitect.breakbadhabits.screens.appDashboard.AppDashboardScreen
import epicarchitect.breakbadhabits.uikit.FlowStateContainer
import epicarchitect.breakbadhabits.uikit.stateOfOneOrNull
import epicarchitect.breakbadhabits.uikit.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.uikit.theme.AppTheme

@Composable
fun App() {
    FlowStateContainer(
        state = stateOfOneOrNull { Environment.database.appSettingsQueries.settings() }
    ) { settings ->
        if (settings != null) {
            AppTheme(
                colorScheme = AppColorsSchemes.byAppSettings(settings)
            ) {
                Navigator(AppDashboardScreen())
            }
        }
    }
}