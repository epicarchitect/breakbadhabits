package breakbadhabits.android.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.navArgument
import breakbadhabits.android.app.format.HabitAbstinenceFormatter
import breakbadhabits.android.app.format.HabitTrackRangeFormatter
import breakbadhabits.android.app.screen.AppSettingsScreen
import breakbadhabits.android.app.screen.HabitCreationScreen
import breakbadhabits.android.app.screen.HabitScreen
import breakbadhabits.android.app.screen.HabitsScreen
import breakbadhabits.app.ui.resources.HabitIconResources
import breakbadhabits.entity.Habit
import epicarchitect.epicstore.compose.RootEpicStore
import epicarchitect.epicstore.navigation.compose.EpicHavHost
import epicarchitect.epicstore.navigation.compose.LocalNavController
import epicarchitect.epicstore.navigation.compose.epicStoreComposable

@Composable
fun App() {
    CompositionLocalProvider(
        LocalHabitIconResources provides HabitIconResources(LocalContext.current),
        LocalHabitAbstinenceFormatter provides HabitAbstinenceFormatter(LocalContext.current),
        LocalHabitTrackRangeFormatter provides HabitTrackRangeFormatter()
    ) {
        RootEpicStore {
            EpicHavHost(
                startDestination = "habits"
            ) {
                epicStoreComposable(route = "appSettings") {
                    val navController = LocalNavController.current
                    AppSettingsScreen(
                        openWidgetSettings = {
                            navController.navigate("habitsAppWidgets")
                        }
                    )
                }

                epicStoreComposable(route = "habitCreation") {
                    val navController = LocalNavController.current
                    HabitCreationScreen(
                        onFinished = {
                            navController.popBackStack()
                        }
                    )
                }

                epicStoreComposable(
                    route = "habitEditing?habitId={habitId}",
                    arguments = listOf(navArgument("habitId") { type = NavType.IntType })
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

                epicStoreComposable(
                    route = "habitEventCreation?habitId={habitId}",
                    arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                ) {
//                    val navController = LocalNavController.current
//                    val habitId = it.arguments!!.getInt("habitId")
//                    HabitEventCreationScreen(
//                        habitId = habitId,
//                        onFinished = {
//                            navController.popBackStack()
//                        }
//                    )
                }

                epicStoreComposable(
                    route = "habitEventEditing?habitEventId={habitEventId}",
                    arguments = listOf(navArgument("habitEventId") { type = NavType.IntType })
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

                epicStoreComposable(
                    route = "habitsAppWidgetConfigEditing?configId={configId}",
                    arguments = listOf(navArgument("configId") { type = NavType.IntType })
                ) {
//                    val navController = LocalNavController.current
//                    HabitsAppWidgetConfigEditingScreen(
//                        configId = it.arguments!!.getInt("configId"),
//                        onFinished = navController::popBackStack
//                    )
                }

                epicStoreComposable(route = "habitsAppWidgets") {
//                    val navController = LocalNavController.current
//                    HabitsAppWidgetsScreen(
//                        openHabitAppWidgetConfigEditing = { configId ->
//                            navController.navigate("habitsAppWidgetConfigEditing?configId=$configId")
//                        }
//                    )
                }

                epicStoreComposable(
                    route = "habit?habitId={habitId}",
                    arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                ) {
                    val navController = LocalNavController.current
                    val habitId = it.arguments!!.getInt("habitId")
                    HabitScreen(
                        habitId = Habit.Id(habitId),
                        openHabitEventCreation = {
                            navController.navigate("habitEventCreation?habitId=$habitId")
                        },
                        openHabitEventEditing = { habitEventId ->
                            navController.navigate("habitEventEditing?habitEventId=$habitEventId")
                        },
                        openHabitEditing = {
                            navController.navigate("habitEditing?habitId=$habitId")
                        },
                        showALlEvents = {
                            navController.navigate("habitEvents?habitId=$habitId")
                        }
                    )
                }

                epicStoreComposable(
                    route = "habitEvents?habitId={habitId}",
                    arguments = listOf(navArgument("habitId") { type = NavType.IntType })
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

                epicStoreComposable(route = "habits") {
                    val navController = LocalNavController.current
                    HabitsScreen(
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
    }
}