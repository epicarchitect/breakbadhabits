package epicarchitect.breakbadhabits.screens.root.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.screens.LocalAppModule
import epicarchitect.breakbadhabits.screens.habits.HabitDetails
import epicarchitect.breakbadhabits.screens.hold

class HabitDetailsScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = hold {
            presentationModule.habits.createHabitDetailsViewModel(habitId)
        }

        HabitDetails(
            habitController = viewModel.habitController,
            habitAbstinenceController = viewModel.habitAbstinenceController,
            abstinenceListController = viewModel.abstinenceListController,
            statisticsController = viewModel.statisticsController,
            currentMonthDailyCountsController = viewModel.currentMonthDailyCountsController,
            onEditClick = {
                navigator += HabitUpdatingScreen(habitId)
            },
            onAddTrackClick = {
                navigator += HabitTrackCreationScreen(habitId)
            },
            onAllTracksClick = {
                navigator += HabitTracksScreen(habitId)
            },
        )
    }
}