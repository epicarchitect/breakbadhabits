package epicarchitect.breakbadhabits.defaultDependencies.habits.creation

import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.features.habits.creation.HabitCreationNavigation

class DefaultHabitCreationNavigation(private val navigator: Navigator) : HabitCreationNavigation {
    override fun back() {
        navigator.pop()
    }
}