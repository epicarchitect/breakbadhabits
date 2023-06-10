package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.foundation.uikit.effect.LaunchedEffectWhenExecuted
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.screens.habits.tracks.HabitTrackCreation
import epicarchitect.breakbadhabits.screens.hold

class HabitTrackCreationScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = hold {
            presentationModule.habits.createHabitTrackCreationViewModel(habitId)
        }

        LaunchedEffectWhenExecuted(viewModel.creationController, navigator::pop)

        HabitTrackCreation(
            eventCountInputController = viewModel.eventCountInputController,
            timeInputController = viewModel.timeInputController,
            creationController = viewModel.creationController,
            habitController = viewModel.habitController,
            commentInputController = viewModel.commentInputController
        )
    }
}