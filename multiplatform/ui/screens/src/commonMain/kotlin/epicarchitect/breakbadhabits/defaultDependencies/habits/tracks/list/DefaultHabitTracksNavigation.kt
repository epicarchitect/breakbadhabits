package epicarchitect.breakbadhabits.defaultDependencies.habits.tracks.list

import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.defaultDependencies.habits.tracks.creation.HabitTrackCreationScreen
import epicarchitect.breakbadhabits.defaultDependencies.habits.tracks.editing.HabitTrackEditingScreen
import epicarchitect.breakbadhabits.features.habits.tracks.list.HabitTracksNavigation

class DefaultHabitTracksNavigation(
    private val navigator: Navigator,
    private val habitId: Int
) : HabitTracksNavigation {
    override fun openTrackCreation() {
        navigator += HabitTrackCreationScreen(habitId)
    }

    override fun openTrackEditing(habitTrackId: Int) {
        navigator += HabitTrackEditingScreen(habitTrackId)
    }

    override fun back() {
        navigator.pop()
    }
}