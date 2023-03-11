package breakbadhabits.android.app.ui.app

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import breakbadhabits.android.app.ui.settings.AppSettingsScreen
import breakbadhabits.app.entity.Habit
import breakbadhabits.foundation.controller.RequestController


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

            val creationState by viewModel.creationController.state.collectAsState()

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

            val updatingState by viewModel.updatingController.state.collectAsState()
            val deletionState by viewModel.deletionController.state.collectAsState()

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
            val habitTrackCreationViewModel = viewModel {
                presentationModule.createHabitTrackCreationViewModel(habitId)
            }
            val habitDetailsViewModel = viewModel {
                presentationModule.createHabitDetailsViewModel(habitId)
            }
            val creationState by habitTrackCreationViewModel.creationController.state.collectAsState()

            LaunchedEffect(creationState) {
                if (creationState.requestState is RequestController.RequestState.Executed) {
                    navController.popBackStack()
                }
            }

            HabitTrackCreationScreen(
                eventCountInputController = habitTrackCreationViewModel.eventCountInputController,
                rangeInputController = habitTrackCreationViewModel.rangeInputController,
                creationController = habitTrackCreationViewModel.creationController,
                habitController = habitDetailsViewModel.habitController,
                commentInputController = habitTrackCreationViewModel.commentInputController
            )
        }

        composable(
            route = "habitEventEditing?habitEventId={habitEventId}",
            arguments = listOf(navArgument("habitEventId") { type = NavType.LongType })
        ) {
//                    val navController = LocalNavController.current
//                    val habitEventId = it.arguments!!.getInt("habitEventId")
//                    HabitEventEditingScreen(
//                        habitEventId = habitEventId,
//                        onFinished = {
//                            navController.popBackStack()
//                        },
//                        onHabitEventDeleted = {
//                            navController.popBackStack()
//                        }
//                    )
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
                }
            )
        }

        composable(
            route = "habitEvents?habitId={habitId}",
            arguments = listOf(navArgument("habitId") { type = NavType.LongType })
        ) {
//                    val navController = LocalNavController.current
//                    val habitId = it.arguments!!.getInt("habitId")
//                    HabitEventsScreen(
//                        habitId = habitId,
//                        openHabitEventEditing = { habitEventId ->
//                            navController.navigate("habitEventEditing?habitEventId=$habitEventId")
//                        }
//                    )
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