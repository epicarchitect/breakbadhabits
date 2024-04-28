package epicarchitect.breakbadhabits.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class HabitTracksScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
//        CompositionLocalProvider(
//            LocalHabitTracksResources provides habitTracksResourcesOf(Locale.current)
//        ) {
//            val navigator = LocalNavigator.currentOrThrow
//            val presentationModule = LocalAppModule.current.presentation
//            val viewModel = hold {
//                presentationModule.habits.habitTracksViewModel(habitId)
//            }
//
//            HabitTracks(
//                viewModel = viewModel,
//                onTrackClick = {
//                    navigator += HabitTrackUpdatingScreen(it)
//                },
//                onAddClick = {
//                    navigator += HabitTrackCreationScreen(habitId)
//                }
//            )
//        }
    }
}