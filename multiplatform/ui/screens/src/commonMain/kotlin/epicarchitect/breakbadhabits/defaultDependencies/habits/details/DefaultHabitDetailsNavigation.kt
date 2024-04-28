package epicarchitect.breakbadhabits.defaultDependencies.habits.details

import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.defaultDependencies.habits.editing.HabitEditingScreen
import epicarchitect.breakbadhabits.defaultDependencies.habits.tracks.creation.HabitTrackCreationScreen
import epicarchitect.breakbadhabits.features.habits.details.HabitDetailsNavigation

class DefaultHabitDetailsNavigation(
    private val navigator: Navigator,
    private val habitId: Int
) : HabitDetailsNavigation {
    override fun openHabitTrackCreation() {
        navigator += HabitTrackCreationScreen(habitId)
    }

    override fun openEditingScreen() {
        navigator += HabitEditingScreen(habitId)
    }

    override fun back() {
        navigator.pop()
    }
}