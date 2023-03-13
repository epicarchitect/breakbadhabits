package breakbadhabits.android.app.ui.app

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import breakbadhabits.android.app.ui.dashboard.DashboardScreen
import breakbadhabits.android.app.ui.habits.HabitCreationScreen
import breakbadhabits.android.app.ui.habits.HabitDetailsScreen
import breakbadhabits.android.app.ui.habits.HabitEditingScreen
import breakbadhabits.android.app.ui.habits.HabitTrackCreationScreen
import breakbadhabits.android.app.ui.habits.HabitTrackUpdatingScreen
import breakbadhabits.android.app.ui.habits.HabitTracksScreen
import breakbadhabits.android.app.ui.settings.AppSettingsScreen
import breakbadhabits.app.entity.Habit
import breakbadhabits.app.entity.HabitTrack
import breakbadhabits.foundation.controller.RequestController
import breakbadhabits.foundation.uikit.ext.collectState


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
}

@Composable
fun AppRootScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Dashboard.route
    ) {
        composable(route = Screens.AppSettings.route) {
            AppSettingsScreen(
                openWidgetSettings = {
                    navController.navigate("habitsAppWidgets")
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
                if (creationState.requestState is RequestController.RequestState.Executed) {
                    navController.popBackStack()
                }
            }

            HabitCreationScreen(
                habitIconSelectionController = viewModel.habitIconSelectionController,
                habitNameController = viewModel.habitNameController,
                firstTrackEventCountInputController = viewModel.firstTrackEventCountInputController,
                firstTrackRangeInputController = viewModel.firstTrackRangeInputController,
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
                if (deletionState.requestState is RequestController.RequestState.Executed) {
                    navController.popBackStack(
                        route = Screens.Dashboard.route,
                        inclusive = false
                    )
                }
            }

            LaunchedEffect(updatingState.requestState) {
                if (updatingState.requestState is RequestController.RequestState.Executed) {
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
                if (creationState.requestState is RequestController.RequestState.Executed) {
                    navController.popBackStack()
                }
            }

            HabitTrackCreationScreen(
                eventCountInputController = viewModel.eventCountInputController,
                rangeInputController = viewModel.rangeInputController,
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
                if (deletionState.requestState is RequestController.RequestState.Executed) {
                    navController.popBackStack()
                }
            }

            LaunchedEffect(updatingState.requestState) {
                if (updatingState.requestState is RequestController.RequestState.Executed) {
                    navController.popBackStack()
                }
            }

            HabitTrackUpdatingScreen(
                eventCountInputController = viewModel.eventCountInputController,
                rangeInputController = viewModel.rangeInputController,
                updatingController = viewModel.updatingController,
                deletionController = viewModel.deletionController,
                habitController = viewModel.habitController,
                commentInputController = viewModel.commentInputController
            )
        }

        composable(
            route = "habitsAppWidgetConfigEditing?configId={configId}",
            arguments = listOf(navArgument("configId") { type = NavType.LongType })
        ) {
//                    val navController = LocalNavController.current
//                    HabitsAppWidgetConfigEditingScreen(
//                        configId = it.arguments!!.getInt("configId"),
//                        onFinished = navController::popBackStack
//                    )
        }

        composable(route = "habitsAppWidgets") {
//                    val navController = LocalNavController.current
//                    HabitsAppWidgetsScreen(
//                        openHabitAppWidgetConfigEditing = { configId ->
//                            navController.navigate("habitsAppWidgetConfigEditing?configId=$configId")
//                        }
//                    )
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