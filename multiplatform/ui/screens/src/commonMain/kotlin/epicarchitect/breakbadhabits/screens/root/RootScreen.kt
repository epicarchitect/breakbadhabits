package epicarchitect.breakbadhabits.screens.root

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.di.declaration.AppModule
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppColorsSchemes
import epicarchitect.breakbadhabits.foundation.uikit.theme.AppTheme
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.screens.root.navigation.DashboardScreen

@Composable
fun RootScreen(appModule: AppModule) {
    CompositionLocalProvider(
        LocalAppModule provides appModule
    ) {
        AppTheme(
            colorScheme = if (isSystemInDarkTheme()) {
                AppColorsSchemes.dark
            } else {
                AppColorsSchemes.light
            }
        ) {
            Navigator(DashboardScreen())
        }
    }
}