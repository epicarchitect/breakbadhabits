package breakbadhabits.app.android

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
import breakbadhabits.app.entity.Habit
import breakbadhabits.foundation.controller.RequestController

@Composable
fun AppRootScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "habits"
    ) {
        composable(route = "appSettings") {
            AppSettingsScreen(
                openWidgetSettings = {
                    navController.navigate("habitsAppWidgets")
                }
            )
        }

        composable(route = "habitCreation") {
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
            route = "habitEditing?habitId={habitId}",
            arguments = listOf(navArgument("habitId") { type = NavType.LongType })
        ) {
//                    val navController = LocalNavController.current
//                    val habitId = it.arguments!!.getInt("habitId")
//                    HabitEditingScreen(
//                        habitId = habitId,
//                        onFinished = {
//                            navController.popBackStack()
//                        },
//                        onHabitDeleted = {
//                            navController.popBackStack(route = "habits", inclusive = false)
//                        }
//                    )
        }

        composable(
            route = "habitEventCreation?habitId={habitId}",
            arguments = listOf(navArgument("habitId") { type = NavType.LongType })
        ) {
            val presentationModule = LocalPresentationModule.current
            val habitId = it.arguments!!.getLong("habitId")
            val viewModel = viewModel {
                presentationModule.createHabitTrackCreationViewModel(Habit.Id(habitId))
            }
            val creationState by viewModel.creationController.state.collectAsState()

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
            route = "habit?habitId={habitId}",
            arguments = listOf(navArgument("habitId") { type = NavType.LongType })
        ) {
            val habitId = it.arguments!!.getLong("habitId")
            val presentationModule = LocalPresentationModule.current
            val viewModel = viewModel {
                presentationModule.createHabitDetailsViewModel(Habit.Id(habitId))
            }

            HabitDetailsScreen(
                habitController = viewModel.habitController
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

        composable(route = "habits") {
            val presentationModule = LocalPresentationModule.current
            val viewModel = viewModel {
                presentationModule.createDashboardViewModel()
            }

            DashboardScreen(
                habitItemsController = viewModel.habitItemsController,
                openHabit = { habitId ->
                    navController.navigate("habit?habitId=${habitId.value}")
                },
                openHabitEventCreation = { habitId ->
                    navController.navigate("habitEventCreation?habitId=${habitId.value}")
                },
                openHabitCreation = {
                    navController.navigate("habitCreation")
                },
                openSettings = {
                    navController.navigate("appSettings")
                }
            )
        }
    }
}