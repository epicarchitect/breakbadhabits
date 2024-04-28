package epicarchitect.breakbadhabits.defaultDependencies.habits.widgets

import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.defaultDependencies.habits.widgets.editing.HabitWidgetEditingScreen
import epicarchitect.breakbadhabits.features.habits.widgets.HabitWidgetsNavigation

class DefaultHabitWidgetsNavigation(private val navigator: Navigator) : HabitWidgetsNavigation {
    override fun openWidget(widgetId: Int) {
        navigator += HabitWidgetEditingScreen(widgetId)
    }

    override fun back() {
        navigator.pop()
    }
}