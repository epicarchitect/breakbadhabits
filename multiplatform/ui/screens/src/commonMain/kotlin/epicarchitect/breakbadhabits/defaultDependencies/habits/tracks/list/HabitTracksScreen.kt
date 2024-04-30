package epicarchitect.breakbadhabits.defaultDependencies.habits.tracks.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.features.LocalAppModule
import epicarchitect.breakbadhabits.features.habits.tracks.list.HabitTracks
import epicarchitect.breakbadhabits.features.habits.tracks.list.HabitTracksDependencies

class HabitTracksScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        HabitTracks(
            dependencies = HabitTracksDependencies(
                habitId = habitId,
                mainDatabase = LocalAppModule.current.mainDatabase,
                navigation = DefaultHabitTracksNavigation(LocalNavigator.currentOrThrow, habitId),
                resources = LocalizedHabitTracksResources(Locale.current)
            )
        )
    }
}