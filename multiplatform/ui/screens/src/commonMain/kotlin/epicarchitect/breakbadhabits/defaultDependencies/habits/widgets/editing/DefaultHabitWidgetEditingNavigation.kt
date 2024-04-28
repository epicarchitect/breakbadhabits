package epicarchitect.breakbadhabits.defaultDependencies.habits.widgets.editing

import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.features.habits.widgets.editing.HabitWidgetEditingNavigation

class DefaultHabitWidgetEditingNavigation(private val navigator: Navigator) : HabitWidgetEditingNavigation {
    override fun back() {
        navigator.pop()
    }
}