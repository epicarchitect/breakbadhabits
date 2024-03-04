package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class HabitTrackCreationScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
//        CompositionLocalProvider(
//            LocalHabitTrackCreationResources provides habitTrackCreationResourcesOf(Locale.current)
//        ) {
//            val navigator = LocalNavigator.currentOrThrow
//            val presentationModule = LocalAppModule.current.presentation
//            val viewModel = hold {
//                presentationModule.habits.habitTrackCreationViewModel(habitId)
//            }
//
//            LaunchedEffectWhenExecuted(viewModel.creationController, navigator::pop)
//
//            HabitTrackCreation(viewModel)
//        }
    }
}