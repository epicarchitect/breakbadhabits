package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

class HabitUpdatingScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
//        val navigator = LocalNavigator.currentOrThrow
//        val presentationModule = LocalAppModule.current.presentation
//        val viewModel = hold {
//            presentationModule.habits.habitUpdatingViewModel(habitId)
//        }
//
//        LaunchedEffectWhenExecuted(viewModel.updatingController, navigator::pop)
//        LaunchedEffectWhenExecuted(viewModel.deletionController) {
//            navigator.popUntil { it is DashboardScreen }
//        }
//
//        HabitEditing(viewModel)
    }
}