package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.screens.appSettingsResourcesOf
import epicarchitect.breakbadhabits.screens.settings.AppSettings
import epicarchitect.breakbadhabits.screens.settings.LocalAppSettingsResources

class AppSettingsScreen : Screen {
    @Composable
    override fun Content() {
        CompositionLocalProvider(
            LocalAppSettingsResources provides appSettingsResourcesOf(Locale.current)
        ) {
            val navigator = LocalNavigator.currentOrThrow

            AppSettings(
                openWidgetSettings = {
                    navigator += HabitAppWidgetsScreen()
                }
            )
        }
    }
}
