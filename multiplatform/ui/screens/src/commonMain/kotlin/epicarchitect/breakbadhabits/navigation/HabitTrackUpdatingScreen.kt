package epicarchitect.breakbadhabits.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class HabitTrackUpdatingScreen(private val habitTrackId: Int) : Screen {
    @Composable
    override fun Content() {
//        val navigator = LocalNavigator.currentOrThrow
//        val presentationModule = LocalAppModule.current.presentation
//        val viewModel = hold {
//            presentationModule.habits.habitTrackUpdatingViewModel(habitTrackId)
//        }
//
//        LaunchedEffectWhenExecuted(viewModel.updatingController, navigator::pop)
//        LaunchedEffectWhenExecuted(viewModel.deletionController, navigator::pop)
//
//        HabitTrackUpdating(viewModel)
    }
}