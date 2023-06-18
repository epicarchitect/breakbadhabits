package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.foundation.uikit.effect.LaunchedEffectWhenExecuted
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.screens.habits.tracks.HabitTrackUpdating
import epicarchitect.breakbadhabits.screens.hold

class HabitTrackUpdatingScreen(private val habitTrackId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = hold {
            presentationModule.habits.habitTrackUpdatingViewModel(habitTrackId)
        }

        LaunchedEffectWhenExecuted(viewModel.updatingController, navigator::pop)
        LaunchedEffectWhenExecuted(viewModel.deletionController, navigator::pop)

        HabitTrackUpdating(
            eventCountInputController = viewModel.eventCountInputController,
            timeInputController = viewModel.timeInputController,
            updatingController = viewModel.updatingController,
            deletionController = viewModel.deletionController,
            habitController = viewModel.habitController,
            commentInputController = viewModel.commentInputController
        )
    }
}