package epicarchitect.breakbadhabits.defaultDependencies.habits.widgets.editing

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.features.habits.widgets.editing.HabitWidgetEditing
import epicarchitect.breakbadhabits.features.habits.widgets.editing.HabitWidgetEditingDependencies

class HabitWidgetEditingScreen(private val widgetId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitWidgetEditing(
            dependencies = HabitWidgetEditingDependencies(
                widgetId = widgetId,
                resources = LocalizedHabitWidgetEditingResources(Locale.current),
                navigation = DefaultHabitWidgetEditingNavigation(LocalNavigator.currentOrThrow),
                mainDatabase = LocalAppModule.current.mainDatabase
            )
        )
    }
}