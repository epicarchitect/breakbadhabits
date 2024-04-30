package epicarchitect.breakbadhabits.defaultDependencies.habits.tracks.editing

import cafe.adriel.voyager.navigator.Navigator
import epicarchitect.breakbadhabits.features.habits.tracks.editing.HabitTrackEditingNavigation

class DefaultHabitTrackEditingNavigation(
    private val navigator: Navigator
) : HabitTrackEditingNavigation {
    override fun back() {
        navigator.pop()
    }
}