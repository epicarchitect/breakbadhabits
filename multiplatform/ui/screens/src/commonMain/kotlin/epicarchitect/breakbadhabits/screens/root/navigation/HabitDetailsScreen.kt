package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class HabitDetailsScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
//        val navigator = LocalNavigator.currentOrThrow
//        val presentationModule = LocalAppModule.current.presentation
//        val viewModel = hold {
//            presentationModule.habits.habitDetailsViewModel(habitId)
//        }
//
//        HabitDetails(
//            viewModel = viewModel,
//            onEditClick = {
//                navigator += HabitUpdatingScreen(habitId)
//            },
//            onAddTrackClick = {
//                navigator += HabitTrackCreationScreen(habitId)
//            },
//            onAllTracksClick = {
//                navigator += HabitTracksScreen(habitId)
//            },
//            onBackClick = navigator::pop
//        )
    }
}