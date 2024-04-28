package epicarchitect.breakbadhabits.defaultDependencies.habits.tracks.creation

import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.features.habits.tracks.creation.HabitTrackCreationNavigation

class DefaultHabitTrackCreationNavigation(
    private val navigator: Navigator
) : HabitTrackCreationNavigation {
    override fun back() {
        navigator.pop()
    }
}