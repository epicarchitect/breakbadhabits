package epicarchitect.breakbadhabits.defaultDependencies.appSettings

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.features.appSettings.AppSettings
import epicarchitect.breakbadhabits.features.appSettings.AppSettingsDependencies

class AppSettingsScreen : Screen {
    @Composable
    override fun Content() {
        AppSettings(
            AppSettingsDependencies(
                resources = LocalizedAppSettingsResources(Locale.current),
                navigation = DefaultAppSettingsNavigation(LocalNavigator.currentOrThrow),
                mainDatabase = LocalAppModule.current.mainDatabase
            )
        )
    }
}