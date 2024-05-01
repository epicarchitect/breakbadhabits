package epicarchitect.breakbadhabits

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.database.AppData
import epicarchitect.breakbadhabits.screens.dashboard.DashboardScreen
import epicarchitect.breakbadhabits.uikit.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.uikit.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Composable
fun App() {
    val appSettingsState = remember {
        AppData.mainDatabase.appSettingsQueries
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