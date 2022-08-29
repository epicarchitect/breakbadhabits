@file:Suppress("UNCHECKED_CAST")

package breakbadhabits.android.app.activity

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
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
import breakbadhabits.android.app.utils.composeViewModel
import breakbadhabits.android.app.utils.get
import breakbadhabits.android.compose.activity.ComposeActivity
import epicarchitect.epicstore.compose.RootEpicStore
import epicarchitect.epicstore.navigation.compose.EpicHavHost
import epicarchitect.epicstore.navigation.compose.LocalNavController
import epicarchitect.epicstore.navigation.compose.epicStoreComposable

class MainActivity : ComposeActivity() {

    override val themeResourceId = R.style.Activity

    @Composable
    override fun Content() {
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
                    val navController = LocalNavController.current
                    val habitId = it.arguments!!.getInt("habitId")
                    HabitEditingScreen(
                        habitId = habitId,
                        onFinished = {
                            navController.popBackStack()
                        },
                        onHabitDeleted = {
                            navController.popBackStack(route = "habits", inclusive = false)
                        }
                    )
                }

                epicStoreComposable(
                    route = "habitEventCreation?habitId={habitId}",
                    arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                ) {
                    val navController = LocalNavController.current
                    val habitId = it.arguments!!.getInt("habitId")
                    HabitEventCreationScreen(
                        habitId = habitId,
                        onFinished = {
                            navController.popBackStack()
                        }
                    )
                }

                epicStoreComposable(
                    route = "habitEventEditing?habitEventId={habitEventId}",
                    arguments = listOf(navArgument("habitEventId") { type = NavType.IntType })
                ) {
                    val navController = LocalNavController.current
                    val habitEventId = it.arguments!!.getInt("habitEventId")
                    HabitEventEditingScreen(
                        habitEventId = habitEventId,
                        onFinished = {
                            navController.popBackStack()
                        },
                        onHabitEventDeleted = {
                            navController.popBackStack()
                        }
                    )
                }

                epicStoreComposable(
                    route = "habitsAppWidgetConfigEditing?configId={configId}",
                    arguments = listOf(navArgument("configId") { type = NavType.IntType })
                ) {
                    val navController = LocalNavController.current
                    val configId = it.arguments!!.getInt("configId")
                    HabitsAppWidgetConfigEditingScreen(
                        habitsAppWidgetConfigEditingViewModel = composeViewModel(configId),
                        alertDialogManager = get(),
                        onFinished = {
                            navController.popBackStack()
                        }
                    )
                }

                epicStoreComposable(route = "habitsAppWidgets") {
                    val navController = LocalNavController.current
                    HabitsAppWidgetsScreen(
                        openHabitAppWidgetConfigEditing = { configId ->
                            navController.navigate("habitsAppWidgetConfigEditing?configId=$configId")
                        }
                    )
                }

                epicStoreComposable(
                    route = "habit?habitId={habitId}",
                    arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                ) {
                    val navController = LocalNavController.current
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

                epicStoreComposable(
                    route = "habitEvents?habitId={habitId}",
                    arguments = listOf(navArgument("habitId") { type = NavType.IntType })
                ) {
                    val navController = LocalNavController.current
                    val habitId = it.arguments!!.getInt("habitId")
                    HabitEventsScreen(
                        habitId = habitId,
                        openHabitEventEditing = { habitEventId ->
                            navController.navigate("habitEventEditing?habitEventId=$habitEventId")
                        }
                    )
                }

                epicStoreComposable(route = "habits") {
                    val navController = LocalNavController.current
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
