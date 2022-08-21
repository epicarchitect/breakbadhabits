@file:Suppress("UNCHECKED_CAST")

package breakbadhabits.android.app.activity

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import breakbadhabits.android.app.R
import breakbadhabits.android.app.compose.screen.AppSettingsScreen
import breakbadhabits.android.app.compose.screen.HabitCreationScreen
import breakbadhabits.android.app.compose.screen.HabitEditingScreen
import breakbadhabits.android.app.compose.screen.HabitEventCreationScreen
import breakbadhabits.android.app.compose.screen.HabitEventEditingScreen
import breakbadhabits.android.app.compose.screen.HabitEventsScreen
import breakbadhabits.android.app.compose.screen.HabitScreen
import breakbadhabits.android.app.compose.screen.HabitsAppWidgetConfigEditingScreen
import breakbadhabits.android.app.compose.screen.HabitsAppWidgetsScreen
import breakbadhabits.android.app.compose.screen.HabitsScreen
import epicarchitect.epicstore.compose.RootEpicStore
import breakbadhabits.android.app.utils.composeViewModel
import breakbadhabits.android.app.utils.get
import breakbadhabits.android.compose.activity.ComposeActivity

class MainActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
        RootEpicStore {
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
                    HabitCreationScreen(
                        habitCreationViewModel = composeViewModel(),
                        dateTimeFormatter = get(),
                        habitIconResources = get(),
                        onFinished = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(
                    route = "habitEditing?habitId={habitId}",
                    arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                ) {
                    val habitId = it.arguments!!.getInt("habitId")
                    HabitEditingScreen(
                        habitEditingViewModel = composeViewModel(habitId),
                        habitIconResources = get(),
                        alertDialogManager = get(),
                        onFinished = {
                            navController.popBackStack()
                        },
                        habitDeletionViewModel = composeViewModel(habitId),
                        onHabitDeleted = {
                            navController.popBackStack(route = "habits", inclusive = false)
                        }
                    )
                }

                composable(
                    route = "habitEventCreation?habitId={habitId}",
                    arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                ) {
                    val habitId = it.arguments!!.getInt("habitId")
                    HabitEventCreationScreen(
                        habitViewModel = composeViewModel(habitId),
                        habitEventCreationViewModel = composeViewModel(habitId),
                        dateTimeFormatter = get(),
                        onFinished = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(
                    route = "habitEventEditing?habitEventId={habitEventId}",
                    arguments = listOf(navArgument("habitEventId") { type = NavType.IntType })
                ) {
                    val habitEventId = it.arguments!!.getInt("habitEventId")
                    HabitEventEditingScreen(
                        habitEventEditingViewModel = composeViewModel(habitEventId),
                        dateTimeFormatter = get(),
                        alertDialogManager = get(),
                        onFinished = {
                            navController.popBackStack()
                        },
                        onHabitEventDeleted = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(
                    route = "habitsAppWidgetConfigEditing?configId={configId}",
                    arguments = listOf(navArgument("configId") { type = NavType.IntType })
                ) {
                    val configId = it.arguments!!.getInt("configId")
                    HabitsAppWidgetConfigEditingScreen(
                        habitsAppWidgetConfigEditingViewModel = composeViewModel(configId),
                        alertDialogManager = get(),
                        onFinished = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("habitsAppWidgets") {
                    HabitsAppWidgetsScreen(
                        widgetsViewModel = composeViewModel(),
                        openHabitAppWidgetConfigEditing = { configId ->
                            navController.navigate("habitsAppWidgetConfigEditing?configId=$configId")
                        }
                    )
                }

                composable(
                    route = "habit?habitId={habitId}",
                    arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                ) {
                    val habitId = it.arguments!!.getInt("habitId")
                    HabitScreen(
                        habitAnalyzeViewModel = composeViewModel(habitId),
                        habitViewModel = composeViewModel(habitId),
                        habitEventsViewModel = composeViewModel(habitId),
                        abstinenceTimeFormatter = get(),
                        dateTimeFormatter = get(),
                        openHabitEventCreation = {
                            navController.navigate("habitEventCreation?habitId=$habitId")
                        },
                        openHabitEventEditing = { habitEventId ->
                            navController.navigate("habitEventEditing?habitEventId=$habitEventId")
                        },
                        openHabitEditing = {
                            navController.navigate("habitEditing?habitId=$habitId")
                        },
                        habitIconResources = get(),
                        showALlEvents = {
                            navController.navigate("habitEvents?habitId=$habitId")
                        }
                    )
                }

                composable(
                    route = "habitEvents?habitId={habitId}",
                    arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                ) {
                    val habitId = it.arguments!!.getInt("habitId")
                    HabitEventsScreen(
                        habitViewModel = composeViewModel(habitId),
                        habitEventsViewModel = composeViewModel(habitId),
                        dateTimeFormatter = get(),
                        habitIconResources = get(),
                        openHabitEventEditing = { habitEventId ->
                            navController.navigate("habitEventEditing?habitEventId=$habitEventId")
                        }
                    )
                }

                composable("habits") {
                    HabitsScreen(
                        habitIconResources = get(),
                        abstinenceTimeFormatter = get(),
                        openHabit = { habitId ->
                            navController.navigate("habit?habitId=$habitId")
                        },
                        openHabitEventCreation = { habitId ->
                            navController.navigate("habitEventCreation?habitId=$habitId")
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
    }
}
