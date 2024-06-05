package epicarchitect.breakbadhabits.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.data.AppData
import epicarchitect.breakbadhabits.operation.sqldelight.flowOfOneOrNull
import epicarchitect.breakbadhabits.ui.component.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.ui.component.theme.AppTheme
import epicarchitect.breakbadhabits.ui.screen.dashboard.DashboardScreen

@Composable
fun Root() {
    val appSettingsState = remember {
        AppData.database.appSettingsQueries.settings().flowOfOneOrNull()
    }.collectAsState(initial = null)

    val appSettings = appSettingsState.value ?: return

    AppTheme(
        colorScheme = AppColorsSchemes.byAppSettings(appSettings)
    ) {
        Navigator(DashboardScreen())
    }
}