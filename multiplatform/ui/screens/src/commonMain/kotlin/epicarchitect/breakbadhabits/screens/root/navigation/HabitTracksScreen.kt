package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.screens.habits.tracks.HabitTracks
import epicarchitect.breakbadhabits.screens.hold

class HabitTracksScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = hold {
            presentationModule.habits.createHabitTracksViewModel(habitId)
        }

        HabitTracks(
            habitController = viewModel.habitController,
            habitTracksController = viewModel.habitTracksController,
            onTrackClick = {
                navigator += HabitTrackUpdatingScreen(it)
            },
            onAddClick = {
                navigator += HabitTrackCreationScreen(habitId)
            }
        )
    }
}