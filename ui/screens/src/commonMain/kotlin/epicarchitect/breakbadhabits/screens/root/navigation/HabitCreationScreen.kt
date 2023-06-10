package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.intl.Locale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.foundation.uikit.effect.LaunchedEffectWhenExecuted
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.screens.habitCreationResourcesOf
import epicarchitect.breakbadhabits.screens.habits.HabitCreation
import epicarchitect.breakbadhabits.screens.habits.LocalHabitCreationResourcesResources
import epicarchitect.breakbadhabits.screens.hold

class HabitCreationScreen : Screen {
    @Composable
    override fun Content() {
        CompositionLocalProvider(
            LocalHabitCreationResourcesResources provides habitCreationResourcesOf(Locale.current)
        ) {
            val navigator = LocalNavigator.currentOrThrow
            val presentationModule = LocalAppModule.current.presentation
            val viewModel = hold {
                presentationModule.habits.createHabitCreationViewModel()
            }

            LaunchedEffectWhenExecuted(viewModel.creationController, navigator::pop)

            HabitCreation(
                habitIconSelectionController = viewModel.habitIconSelectionController,
                habitNameController = viewModel.habitNameController,
                dailyEventCountInputController = viewModel.dailyEventCountInputController,
                firstTrackTimeInputController = viewModel.firstTrackTimeInputController,
                creationController = viewModel.creationController
            )
        }
    }
}