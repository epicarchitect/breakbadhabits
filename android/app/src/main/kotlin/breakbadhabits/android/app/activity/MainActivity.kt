@file:Suppress("UNCHECKED_CAST")

package breakbadhabits.android.app.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import breakbadhabits.android.app.App
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
import breakbadhabits.android.app.utils.NightModeManager
import breakbadhabits.compose.theme.BreakBadHabitsTheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Activity)
        setContent {
            BreakBadHabitsTheme(
                isDarkTheme = when (App.architecture.nightModeManager.mode) {
                    NightModeManager.Mode.NIGHT -> true
                    NightModeManager.Mode.NOT_NIGHT -> false
                    NightModeManager.Mode.FOLLOW_SYSTEM -> isSystemInDarkTheme()
                }
            ) {
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
                            onFinished = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(
                        route = "habitEditing?habitId={habitId}",
                        arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                    ) {
                        HabitEditingScreen(
                            habitId = it.arguments!!.getInt("habitId"),
                            onFinished = {
                                navController.popBackStack()
                            },
                            onHabitDeleted = {
                                navController.popBackStack(
                                    route = "habits",
                                    inclusive = false
                                )
                            }
                        )
                    }

                    composable(
                        route = "habitEventCreation?habitId={habitId}",
                        arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                    ) {
                        HabitEventCreationScreen(
                            habitId = it.arguments!!.getInt("habitId"),
                            onFinished = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable(
                        route = "habitEventEditing?habitEventId={habitEventId}",
                        arguments = listOf(navArgument("habitEventId") { type = NavType.IntType })
                    ) {
                        HabitEventEditingScreen(
                            habitEventId = it.arguments!!.getInt("habitEventId"),
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
                        HabitsAppWidgetConfigEditingScreen(
                            configId = it.arguments!!.getInt("configId"),
                            onFinished = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("habitsAppWidgets") {
                        HabitsAppWidgetsScreen(
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
                            habitId = habitId,
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

                    composable(
                        route = "habitEvents?habitId={habitId}",
                        arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                    ) {
                        HabitEventsScreen(
                            habitId = it.arguments!!.getInt("habitId"),
                            openHabitEventEditing = { habitEventId ->
                                navController.navigate("habitEventEditing?habitEventId=$habitEventId")
                            }
                        )
                    }

                    composable("habits") {
                        HabitsScreen(
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
}
