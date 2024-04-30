package epicarchitect.breakbadhabits.defaultDependencies.appSettings

import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.defaultDependencies.habits.widgets.list.HabitWidgetsScreen
import epicarchitect.breakbadhabits.features.appSettings.AppSettingsNavigation

class DefaultAppSettingsNavigation(private val navigator: Navigator) : AppSettingsNavigation {
    override fun back() {
        navigator.pop()
    }

    override fun openAppWidgets() {
        navigator += HabitWidgetsScreen()
    }
}