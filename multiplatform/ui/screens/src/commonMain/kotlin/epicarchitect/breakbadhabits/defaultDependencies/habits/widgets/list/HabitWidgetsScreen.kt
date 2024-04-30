package epicarchitect.breakbadhabits.defaultDependencies.habits.widgets.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.features.habits.widgets.list.HabitAppWidgets
import epicarchitect.breakbadhabits.features.habits.widgets.list.HabitWidgetsDependencies

class HabitWidgetsScreen : Screen {
    @Composable
    override fun Content() {
        HabitAppWidgets(
            dependencies = HabitWidgetsDependencies(
                resources = LocalizedHabitWidgetsResources(Locale.current),
                navigation = DefaultHabitWidgetsNavigation(LocalNavigator.currentOrThrow),
                mainDatabase = LocalAppModule.current.mainDatabase
            )
        )
    }
}