package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.screens.habitTracksResourcesOf
import epicarchitect.breakbadhabits.screens.habits.tracks.HabitTracks
import epicarchitect.breakbadhabits.screens.habits.tracks.LocalHabitTracksResources
import epicarchitect.breakbadhabits.screens.hold

class HabitTracksScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        CompositionLocalProvider(
            LocalHabitTracksResources provides habitTracksResourcesOf(Locale.current)
        ) {
            val navigator = LocalNavigator.currentOrThrow
            val presentationModule = LocalAppModule.current.presentation
            val viewModel = hold {
                presentationModule.habits.habitTracksViewModel(habitId)
            }

            HabitTracks(
                viewModel = viewModel,
                onTrackClick = {
                    navigator += HabitTrackUpdatingScreen(it)
                },
                onAddClick = {
                    navigator += HabitTrackCreationScreen(habitId)
                }
            )
        }
    }
}