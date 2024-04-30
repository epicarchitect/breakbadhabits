package epicarchitect.breakbadhabits.screens.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.database.AppData
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppTheme
import epicarchitect.breakbadhabits.screens.dashboard.DashboardScreen
import kotlinx.coroutines.Dispatchers

@Composable
fun RootScreen() {
    val appSettingsState = remember {
        AppData.mainDatabase.appSettingsQueries.get().asFlow().mapToOneOrNull(Dispatchers.IO)
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