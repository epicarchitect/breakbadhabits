package epicarchitect.breakbadhabits.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.database.AppData
import epicarchitect.breakbadhabits.ui.dashboard.DashboardScreen
import epicarchitect.breakbadhabits.uikit.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.uikit.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Composable
fun Root() {
    val appSettingsState = remember {
        AppData.database.appSettingsQueries
            .get()
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(initial = null)

    val appSettings = appSettingsState.value

    AppTheme(
        colorScheme = when (appSettings?.theme) {
            1L -> AppColorsSchemes.light
            2L -> AppColorsSchemes.dark
            else -> AppColorsSchemes.system
        }
    ) {
        Navigator(DashboardScreen())
    }
}