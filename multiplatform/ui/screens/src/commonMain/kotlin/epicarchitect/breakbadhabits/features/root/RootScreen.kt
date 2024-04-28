package epicarchitect.breakbadhabits.features.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.defaultDependencies.dashboard.DashboardScreen
import epicarchitect.breakbadhabits.di.holder.AppModuleHolder
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppTheme
import kotlinx.coroutines.Dispatchers

@Composable
fun RootScreen() {
    val appModule = AppModuleHolder.require()
    val appSettingsState = remember(appModule) {
        appModule.mainDatabase
            .appSettingsQueries
            .get()
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
    }.collectAsState(initial = null)

    val appSettings = appSettingsState.value

    CompositionLocalProvider(
        LocalAppModule provides appModule
    ) {
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
}