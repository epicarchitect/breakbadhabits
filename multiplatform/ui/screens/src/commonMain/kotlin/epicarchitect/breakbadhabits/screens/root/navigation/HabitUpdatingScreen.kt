package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.foundation.uikit.effect.LaunchedEffectWhenExecuted
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.screens.habits.HabitEditing
import epicarchitect.breakbadhabits.screens.hold

class HabitUpdatingScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = hold {
            presentationModule.habits.habitUpdatingViewModel(habitId)
        }

        LaunchedEffectWhenExecuted(viewModel.updatingController, navigator::pop)
        LaunchedEffectWhenExecuted(viewModel.deletionController) {
            navigator.popUntil { it is DashboardScreen }
        }

        HabitEditing(
            habitNameController = viewModel.habitNameController,
            habitIconSelectionController = viewModel.habitIconSelectionController,
            updatingController = viewModel.updatingController,
            deletionController = viewModel.deletionController
        )
    }
}
