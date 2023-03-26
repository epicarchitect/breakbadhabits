package breakbadhabits.android.app.ui.app

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import breakbadhabits.android.app.di.LocalLogicModule
import breakbadhabits.android.app.di.LocalPresentationModule
import breakbadhabits.android.app.di.LocalUiModule
import breakbadhabits.android.app.di.UiModule
import breakbadhabits.android.app.ui.dashboard.DashboardScreen
import breakbadhabits.android.app.ui.habits.HabitCreationScreen
import breakbadhabits.android.app.ui.habits.HabitDetailsScreen
import breakbadhabits.android.app.ui.habits.HabitEditingScreen
import breakbadhabits.android.app.ui.habits.tracks.HabitTrackCreationScreen
import breakbadhabits.android.app.ui.habits.tracks.HabitTrackUpdatingScreen
import breakbadhabits.android.app.ui.habits.tracks.HabitTracksScreen
import breakbadhabits.android.app.ui.habits.widgets.HabitAppWidgetUpdatingScreen
import breakbadhabits.android.app.ui.habits.widgets.HabitAppWidgetsScreen
import breakbadhabits.android.app.ui.settings.AppSettingsScreen
import breakbadhabits.app.logic.habits.entity.Habit
import breakbadhabits.app.logic.habits.entity.HabitAppWidgetConfig
import breakbadhabits.app.logic.habits.entity.HabitTrack
import breakbadhabits.foundation.controller.SingleRequestController
import breakbadhabits.foundation.uikit.Backlight
import breakbadhabits.foundation.uikit.Card
import breakbadhabits.foundation.uikit.ext.collectState
import breakbadhabits.foundation.uikit.theme.AppTheme


private object Screens {
    object Dashboard {
        const val route = "dashboard"
    }

    object AppSettings {
        const val route = "appSettings"
    }

    object HabitCreation {
        const val route = "habitCreation"
    }

    object HabitDetails {
        const val route = "habit?id={id}"
        val arguments = listOf(navArgument("id") { type = NavType.LongType })
        fun getHabitId(arguments: Bundle?) = Habit.Id(arguments!!.getLong("id"))
        fun buildRoute(id: Habit.Id) = "habit?id=${id.value}"
    }

    object HabitUpdating {
        const val route = "habitUpdating?id={id}"
        val arguments = listOf(navArgument("id") { type = NavType.LongType })
        fun getHabitId(arguments: Bundle?) = Habit.Id(arguments!!.getLong("id"))
        fun buildRoute(id: Habit.Id) = "habitUpdating?id=${id.value}"
    }

    object HabitTrackCreation {
        const val route = "habitTrackCreation?id={id}"
        val arguments = listOf(navArgument("id") { type = NavType.LongType })
        fun getHabitId(arguments: Bundle?) = Habit.Id(arguments!!.getLong("id"))
        fun buildRoute(id: Habit.Id) = "habitTrackCreation?id=${id.value}"
    }

    object HabitTracks {
        const val route = "habitTracks?id={id}"
        val arguments = listOf(navArgument("id") { type = NavType.LongType })
        fun getHabitId(arguments: Bundle?) = Habit.Id(arguments!!.getLong("id"))
        fun buildRoute(id: Habit.Id) = "habitTracks?id=${id.value}"
    }

    object HabitTrackUpdating {
        const val route = "habitTrackUpdating?id={id}"
        val arguments = listOf(navArgument("id") { type = NavType.LongType })
        fun getHabitTrackId(arguments: Bundle?) = HabitTrack.Id(arguments!!.getLong("id"))
        fun buildRoute(id: HabitTrack.Id) = "habitTrackUpdating?id=${id.value}"
    }

    object HabitAppWidgets {
        const val route = "habitAppWidgets"
    }

    object HabitAppWidgetUpdating {
        const val route = "habitAppWidgetUpdating?id={id}"
        val arguments = listOf(navArgument("id") { type = NavType.LongType })
        fun getHabitAppWidgetId(arguments: Bundle?) =
            HabitAppWidgetConfig.Id(arguments!!.getLong("id"))

        fun buildRoute(id: HabitAppWidgetConfig.Id) = "habitAppWidgetUpdating?id=${id.value}"
    }
}

@Composable
fun AppScreen(uiModule: UiModule) {
    CompositionLocalProvider(
        LocalUiModule provides uiModule,
        LocalPresentationModule provides uiModule.presentationModule,
        LocalLogicModule provides uiModule.presentationModule.logicModule
    ) {
        Box {
            if (AppTheme.colorScheme.isDark) {
                Backlight()
            }

            AppScreenContent()
        }
    }
}

@Composable
private fun AppScreenContent() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Dashboard.route
    ) {
        composable(route = Screens.AppSettings.route) {
            AppSettingsScreen(
                openWidgetSettings = {
                    navController.navigate(Screens.HabitAppWidgets.route)
                }
            )
        }

        composable(route = Screens.HabitCreation.route) {
            val presentationModule = LocalPresentationModule.current
            val viewModel = viewModel {
                presentationModule.createHabitCreationViewModel()
            }

            val creationState by viewModel.creationController.collectState()

            LaunchedEffect(creationState) {
                if (creationState.requestState is SingleRequestController.RequestState.Executed) {
                    navController.popBackStack()
                }
            }

            HabitCreationScreen(
                habitIconSelectionController = viewModel.habitIconSelectionController,
                habitNameController = viewModel.habitNameController,
                firstTrackEventCountInputController = viewModel.firstTrackEventCountInputController,
                firstTrackTimeInputController = viewModel.firstTrackTimeInputController,
                creationController = viewModel.creationController
            )
        }

        composable(
            route = Screens.HabitUpdating.route,
            arguments = Screens.HabitUpdating.arguments
        ) {
            val presentationModule = LocalPresentationModule.current
            val habitId = Screens.HabitUpdating.getHabitId(it.arguments)
            val viewModel = viewModel {
                presentationModule.createHabitUpdatingViewModel(habitId)
            }

            val updatingState by viewModel.updatingController.collectState()
            val deletionState by viewModel.deletionController.collectState()

            LaunchedEffect(deletionState.requestState) {
                if (deletionState.requestState is SingleRequestController.RequestState.Executed) {
                    navController.popBackStack(
                        route = Screens.Dashboard.route,
                        inclusive = false
                    )
                }
            }

            LaunchedEffect(updatingState.requestState) {
                if (updatingState.requestState is SingleRequestController.RequestState.Executed) {
                    navController.popBackStack()
                }
            }

            HabitEditingScreen(
                habitNameController = viewModel.habitNameController,
                habitIconSelectionController = viewModel.habitIconSelectionController,
                updatingController = viewModel.updatingController,
                deletionController = viewModel.deletionController
            )
        }

        composable(
            route = Screens.HabitTrackCreation.route,
            arguments = Screens.HabitTrackCreation.arguments
        ) {
            val presentationModule = LocalPresentationModule.current
            val habitId = Screens.HabitTrackCreation.getHabitId(it.arguments)
            val viewModel = viewModel {
                presentationModule.createHabitTrackCreationViewModel(habitId)
            }
            val creationState by viewModel.creationController.collectState()

            LaunchedEffect(creationState) {
                if (creationState.requestState is SingleRequestController.RequestState.Executed) {
                    navController.popBackStack()
                }
            }

            HabitTrackCreationScreen(
                eventCountInputController = viewModel.eventCountInputController,
                timeInputController = viewModel.timeInputController,
                creationController = viewModel.creationController,
                habitController = viewModel.habitController,
                commentInputController = viewModel.commentInputController
            )
        }

        composable(
            route = Screens.HabitTrackUpdating.route,
            arguments = Screens.HabitTrackUpdating.arguments
        ) {
            val trackId = Screens.HabitTrackUpdating.getHabitTrackId(it.arguments)
            val presentationModule = LocalPresentationModule.current
            val viewModel = viewModel {
                presentationModule.createHabitTrackUpdatingViewModel(trackId)
            }

            val updatingState by viewModel.updatingController.collectState()
            val deletionState by viewModel.deletionController.collectState()

            LaunchedEffect(deletionState.requestState) {
                if (deletionState.requestState is SingleRequestController.RequestState.Executed) {
                    navController.popBackStack()
                }
            }

            LaunchedEffect(updatingState.requestState) {
                if (updatingState.requestState is SingleRequestController.RequestState.Executed) {
                    navController.popBackStack()
                }
            }

            HabitTrackUpdatingScreen(
                eventCountInputController = viewModel.eventCountInputController,
                timeInputController = viewModel.timeInputController,
                updatingController = viewModel.updatingController,
                deletionController = viewModel.deletionController,
                habitController = viewModel.habitController,
                commentInputController = viewModel.commentInputController
            )
        }

        composable(
            route = Screens.HabitAppWidgetUpdating.route,
            arguments = Screens.HabitAppWidgetUpdating.arguments
        ) {
            val presentationModule = LocalPresentationModule.current
            val id = Screens.HabitAppWidgetUpdating.getHabitAppWidgetId(it.arguments)
            val viewModel = viewModel {
                presentationModule.createHabitAppWidgetUpdatingViewModel(id)
            }
            val updatingState by viewModel.updatingController.collectState()
            val deletionState by viewModel.deletionController.collectState()

            LaunchedEffect(deletionState.requestState) {
                if (deletionState.requestState is SingleRequestController.RequestState.Executed) {
                    navController.popBackStack()
                }
            }

            LaunchedEffect(updatingState.requestState) {
                if (updatingState.requestState is SingleRequestController.RequestState.Executed) {
                    navController.popBackStack()
                }
            }

            HabitAppWidgetUpdatingScreen(
                titleInputController = viewModel.titleInputController,
                habitsSelectionController = viewModel.habitsSelectionController,
                updatingController = viewModel.updatingController,
                deletionController = viewModel.deletionController
            )
        }

        composable(route = Screens.HabitAppWidgets.route) {
            val presentationModule = LocalPresentationModule.current
            val viewModel = viewModel {
                presentationModule.createHabitAppWidgetsViewModel()
            }

            HabitAppWidgetsScreen(
                itemsController = viewModel.itemsController,
                onWidgetClick = {
                    navController.navigate(Screens.HabitAppWidgetUpdating.buildRoute(it))
                }
            )
        }

        composable(
            route = Screens.HabitDetails.route,
            arguments = Screens.HabitDetails.arguments
        ) {
            val habitId = Screens.HabitDetails.getHabitId(it.arguments)
            val presentationModule = LocalPresentationModule.current
            val viewModel = viewModel {
                presentationModule.createHabitDetailsViewModel(habitId)
            }

            HabitDetailsScreen(
                habitController = viewModel.habitController,
                habitAbstinenceController = viewModel.habitAbstinenceController,
                abstinenceListController = viewModel.abstinenceListController,
                statisticsController = viewModel.statisticsController,
                habitTracksController = viewModel.habitTracksController,
                onEditClick = {
                    navController.navigate(Screens.HabitUpdating.buildRoute(habitId))
                },
                onAddTrackClick = {
                    navController.navigate(Screens.HabitTrackCreation.buildRoute(habitId))
                },
                onAllTracksClick = {
                    navController.navigate(Screens.HabitTracks.buildRoute(habitId))
                }
            )
        }

        composable(
            route = Screens.HabitTracks.route,
            arguments = Screens.HabitTracks.arguments
        ) {
            val presentationModule = LocalPresentationModule.current
            val habitId = Screens.HabitTracks.getHabitId(it.arguments)
            val viewModel = viewModel {
                presentationModule.createHabitTracksViewModel(habitId)
            }

            HabitTracksScreen(
                habitController = viewModel.habitController,
                habitTracksController = viewModel.habitTracksController,
                onTrackClick = {
                    navController.navigate(Screens.HabitTrackUpdating.buildRoute(it))
                }
            )
        }

        composable(route = Screens.Dashboard.route) {
            val presentationModule = LocalPresentationModule.current
            val viewModel = viewModel {
                presentationModule.createDashboardViewModel()
            }

            DashboardScreen(
                habitItemsController = viewModel.habitItemsController,
                onHabitClick = { habitId ->
                    navController.navigate(Screens.HabitDetails.buildRoute(habitId))
                },
                onAddTrackClick = { habitId ->
                    navController.navigate(Screens.HabitTrackCreation.buildRoute(habitId))
                },
                onHabitCreationClick = {
                    navController.navigate(Screens.HabitCreation.route)
                },
                onAppSettingsClick = {
                    navController.navigate(Screens.AppSettings.route)
                }
            )
        }
    }
}