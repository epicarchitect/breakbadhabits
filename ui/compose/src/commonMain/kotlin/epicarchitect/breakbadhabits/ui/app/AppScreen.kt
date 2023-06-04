package epicarchitect.breakbadhabits.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import epicarchitect.breakbadhabits.di.declaration.AppModule
import epicarchitect.breakbadhabits.di.holder.LocalAppModule
import epicarchitect.breakbadhabits.foundation.uikit.effect.LaunchedEffectWhenExecuted
import epicarchitect.breakbadhabits.ui.dashboard.Dashboard
import epicarchitect.breakbadhabits.ui.habits.HabitCreation
import epicarchitect.breakbadhabits.ui.habits.HabitDetails
import epicarchitect.breakbadhabits.ui.habits.HabitEditing
import epicarchitect.breakbadhabits.ui.habits.tracks.HabitTrackCreation
import epicarchitect.breakbadhabits.ui.habits.tracks.HabitTrackUpdating
import epicarchitect.breakbadhabits.ui.habits.tracks.HabitTracks
import epicarchitect.breakbadhabits.ui.habits.widgets.HabitAppWidgetUpdating
import epicarchitect.breakbadhabits.ui.habits.widgets.HabitAppWidgets
import epicarchitect.breakbadhabits.ui.settings.AppSettings
import epicarchitect.breakbadhabits.ui.viewModel


@Composable
fun AppScreen(appModule: AppModule) {
    CompositionLocalProvider(
        LocalAppModule provides appModule
    ) {
        Navigator(DashboardScreen())
    }
}

class DashboardScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = viewModel {
            presentationModule.dashboard.createDashboardViewModel()
        }

        Dashboard(
            habitItemsController = viewModel.itemsLoadingController,
            onHabitClick = { habitId ->
                navigator += HabitDetailsScreen(habitId)
            },
            onAddTrackClick = { habitId ->
                navigator += HabitTrackCreationScreen(habitId)
            },
            onHabitCreationClick = {
                navigator += HabitCreationScreen()
            },
            onAppSettingsClick = {
                navigator += AppSettingsScreen()
            }
        )
    }
}

class AppSettingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        AppSettings(
            openWidgetSettings = {
                navigator += HabitAppWidgetsScreen()
            }
        )
    }
}

class HabitCreationScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = viewModel {
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

class HabitDetailsScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = viewModel {
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

class HabitUpdatingScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = viewModel {
            presentationModule.habits.createHabitUpdatingViewModel(habitId)
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

class HabitTrackCreationScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = viewModel {
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

class HabitTrackUpdatingScreen(private val habitTrackId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = viewModel {
            presentationModule.habits.createHabitTrackUpdatingViewModel(habitTrackId)
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

class HabitTracksScreen(private val habitId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = viewModel {
            presentationModule.habits.createHabitTracksViewModel(habitId)
        }

        HabitTracks(
            habitController = viewModel.habitController,
            habitTracksController = viewModel.habitTracksController,
            onTrackClick = {
                navigator += HabitTrackUpdatingScreen(it)
            },
            onAddClick = {
                navigator += HabitTrackCreationScreen(habitId)
            }
        )
    }
}

class HabitAppWidgetsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = viewModel {
            presentationModule.habits.createHabitAppWidgetsViewModel()
        }

        HabitAppWidgets(
            itemsController = viewModel.itemsController,
            onWidgetClick = {
                navigator += HabitAppWidgetUpdatingScreen(it)
            }
        )
    }
}

class HabitAppWidgetUpdatingScreen(private val habitWidgetId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val presentationModule = LocalAppModule.current.presentation
        val viewModel = viewModel {
            presentationModule.habits.createHabitWidgetUpdatingViewModel(habitWidgetId)
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