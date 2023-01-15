package breakbadhabits.android.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import breakbadhabits.android.app.screen.AppSettingsScreen
import breakbadhabits.android.app.screen.HabitCreationScreen
import breakbadhabits.android.app.screen.HabitScreen
import breakbadhabits.android.app.screen.HabitsScreen
import breakbadhabits.entity.Habit

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
            HabitCreationScreen(
                onFinished = navController::popBackStack
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
//                    val navController = LocalNavController.current
//                    val habitId = it.arguments!!.getInt("habitId")
//                    HabitEventCreationScreen(
//                        habitId = habitId,
//                        onFinished = {
//                            navController.popBackStack()
//                        }
//                    )
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