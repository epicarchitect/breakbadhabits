package epicarchitect.breakbadhabits.android.app.ui.app

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import epicarchitect.breakbadhabits.android.app.di.LocalLogicModule
import epicarchitect.breakbadhabits.android.app.di.LocalPresentationModule
import epicarchitect.breakbadhabits.android.app.di.LocalUiModule
import epicarchitect.breakbadhabits.android.app.di.UiModule
import epicarchitect.breakbadhabits.android.app.ui.dashboard.DashboardScreen
import epicarchitect.breakbadhabits.android.app.ui.habits.HabitCreationScreen
import epicarchitect.breakbadhabits.android.app.ui.habits.HabitDetailsScreen
import epicarchitect.breakbadhabits.android.app.ui.habits.HabitEditingScreen
import epicarchitect.breakbadhabits.android.app.ui.habits.tracks.HabitTrackCreationScreen
import epicarchitect.breakbadhabits.android.app.ui.habits.tracks.HabitTrackUpdatingScreen
import epicarchitect.breakbadhabits.android.app.ui.habits.tracks.HabitTracksScreen
import epicarchitect.breakbadhabits.android.app.ui.habits.widgets.HabitAppWidgetUpdatingScreen
import epicarchitect.breakbadhabits.android.app.ui.habits.widgets.HabitAppWidgetsScreen
import epicarchitect.breakbadhabits.android.app.ui.settings.AppSettingsScreen
import epicarchitect.breakbadhabits.foundation.uikit.effect.LaunchedEffectWhenExecuted


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
        val arguments = listOf(navArgument("id") { type = NavType.IntType })
        fun getHabitId(arguments: Bundle?) = arguments!!.getInt("id")
        fun buildRoute(habitId: Int) = "habit?id=$habitId"
    }

    object HabitUpdating {
        const val route = "habitUpdating?id={id}"
        val arguments = listOf(navArgument("id") { type = NavType.IntType })
        fun getHabitId(arguments: Bundle?) = arguments!!.getInt("id")
        fun buildRoute(habitId: Int) = "habitUpdating?id=$habitId"
    }

    object HabitTrackCreation {
        const val route = "habitTrackCreation?id={id}"
        val arguments = listOf(navArgument("id") { type = NavType.IntType })
        fun getHabitId(arguments: Bundle?) = arguments!!.getInt("id")
        fun buildRoute(habitId: Int) = "habitTrackCreation?id=$habitId"
    }

    object HabitTracks {
        const val route = "habitTracks?id={id}"
        val arguments = listOf(navArgument("id") { type = NavType.IntType })
        fun getHabitId(arguments: Bundle?) = arguments!!.getInt("id")
        fun buildRoute(habitId: Int) = "habitTracks?id=$habitId"
    }

    object HabitTrackUpdating {
        const val route = "habitTrackUpdating?id={id}"
        val arguments = listOf(navArgument("id") { type = NavType.IntType })
        fun getHabitTrackId(arguments: Bundle?) = arguments!!.getInt("id")
        fun buildRoute(habitTrackId: Int) = "habitTrackUpdating?id=$habitTrackId"
    }

    object HabitAppWidgets {
        const val route = "habitAppWidgets"
    }

    object HabitAppWidgetUpdating {
        const val route = "habitAppWidgetUpdating?id={id}"
        val arguments = listOf(navArgument("id") { type = NavType.IntType })
        fun getHabitAppWidgetId(arguments: Bundle?) = arguments!!.getInt("id")
        fun buildRoute(habitWidgetId: Int) = "habitAppWidgetUpdating?id=$habitWidgetId"
    }
}

@Composable
fun AppScreen(uiModule: UiModule) {
    CompositionLocalProvider(
        LocalUiModule provides uiModule,
        LocalPresentationModule provides uiModule.presentationModule,
        LocalLogicModule provides uiModule.presentationModule.logicModule
    ) {
        AppScreenContent()
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

            LaunchedEffectWhenExecuted(viewModel.creationController, navController::popBackStack)

            HabitCreationScreen(
                habitIconSelectionController = viewModel.habitIconSelectionController,
                habitNameController = viewModel.habitNameController,
                dailyEventCountInputController = viewModel.dailyEventCountInputController,
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

            LaunchedEffectWhenExecuted(viewModel.updatingController, navController::popBackStack)
            LaunchedEffectWhenExecuted(viewModel.deletionController) {
                navController.popBackStack(
                    route = Screens.Dashboard.route,
                    inclusive = false
                )
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

            LaunchedEffectWhenExecuted(viewModel.creationController, navController::popBackStack)

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

            LaunchedEffectWhenExecuted(viewModel.updatingController, navController::popBackStack)
            LaunchedEffectWhenExecuted(viewModel.deletionController, navController::popBackStack)

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
                presentationModule.createHabitWidgetUpdatingViewModel(id)
            }

            LaunchedEffectWhenExecuted(viewModel.updatingController, navController::popBackStack)
            LaunchedEffectWhenExecuted(viewModel.deletionController, navController::popBackStack)

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
                currentMonthDailyCountsController = viewModel.currentMonthDailyCountsController,
                onEditClick = {
                    navController.navigate(Screens.HabitUpdating.buildRoute(habitId))
                },
                onAddTrackClick = {
                    navController.navigate(Screens.HabitTrackCreation.buildRoute(habitId))
                },
                onAllTracksClick = {
                    navController.navigate(Screens.HabitTracks.buildRoute(habitId))
                },
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
                },
                onAddClick = {
                    navController.navigate(Screens.HabitTrackCreation.buildRoute(habitId))
                }
            )
        }

        composable(route = Screens.Dashboard.route) {
            val presentationModule = LocalPresentationModule.current
            val viewModel = viewModel {
                presentationModule.createDashboardViewModel()
            }

            DashboardScreen(
                habitItemsController = viewModel.itemsLoadingController,
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