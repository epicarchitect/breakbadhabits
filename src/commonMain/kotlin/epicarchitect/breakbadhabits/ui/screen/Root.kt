package epicarchitect.breakbadhabits.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.data.database.appSettings.AppSettingsTheme
import epicarchitect.breakbadhabits.operation.sqldelight.flowOfOneOrNull
import epicarchitect.breakbadhabits.ui.screen.dashboard.DashboardScreen
import epicarchitect.breakbadhabits.ui.component.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme

@Composable
fun Root() {
    val appSettingsState = remember {
        AppData.database.appSettingsQueries.settings().flowOfOneOrNull()
    }.collectAsState(initial = null)

    val appSettings = appSettingsState.value ?: return

    AppTheme(
        colorScheme = when (appSettings.theme) {
            AppSettingsTheme.LIGHT -> AppColorsSchemes.light
            AppSettingsTheme.DARK -> AppColorsSchemes.dark
            AppSettingsTheme.SYSTEM -> AppColorsSchemes.system
        }
    ) {
        Navigator(DashboardScreen())
    }
}