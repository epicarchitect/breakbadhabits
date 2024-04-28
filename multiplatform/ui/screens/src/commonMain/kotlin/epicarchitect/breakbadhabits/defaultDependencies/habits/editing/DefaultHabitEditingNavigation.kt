package epicarchitect.breakbadhabits.defaultDependencies.habits.editing

import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.defaultDependencies.dashboard.DashboardScreen
import epicarchitect.breakbadhabits.features.habits.editing.HabitEditingNavigation

class DefaultHabitEditingNavigation(private val navigator: Navigator) : HabitEditingNavigation {
    override fun back() {
        navigator.pop()
    }

    override fun backToDashboard() {
        navigator.popUntil { it is DashboardScreen }
    }
}