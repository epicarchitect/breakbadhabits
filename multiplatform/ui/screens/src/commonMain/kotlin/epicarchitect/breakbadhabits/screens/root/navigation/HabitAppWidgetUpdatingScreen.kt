package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.foundation.uikit.effect.LaunchedEffectWhenExecuted
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.screens.habits.widgets.HabitAppWidgetUpdating
import epicarchitect.breakbadhabits.screens.hold

class HabitAppWidgetUpdatingScreen(private val habitWidgetId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = hold {
            presentationModule.habits.habitWidgetUpdatingViewModel(habitWidgetId)
        }

        LaunchedEffectWhenExecuted(viewModel.updatingController, navigator::pop)
        LaunchedEffectWhenExecuted(viewModel.deletionController, navigator::pop)

        HabitAppWidgetUpdating(
            titleInputController = viewModel.titleInputController,
            habitsSelectionController = viewModel.habitsSelectionController,
            updatingController = viewModel.updatingController,
            deletionController = viewModel.deletionController
        )
    }
}