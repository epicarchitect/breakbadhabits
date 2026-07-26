package epicarchitect.breakbadhabits.screens.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import epicarchitect.breakbadhabits.environment.AppEnvironment
import epicarchitect.breakbadhabits.environment.LocalAppEnvironment
import epicarchitect.breakbadhabits.screens.dashboard.DashboardScreen
import epicarchitect.breakbadhabits.screens.habits.details.HabitDetailsScreen
import epicarchitect.breakbadhabits.screens.habits.editing.HabitEditingScreen
import epicarchitect.breakbadhabits.screens.habits.eventRecords.details.HabitEventRecordsDetailsScreen
import epicarchitect.breakbadhabits.screens.habits.eventRecords.editing.HabitEventRecordEditingScreen

val LocalRootNavController = staticCompositionLocalOf<NavController> {
    error("LocalRootNavController not present")
}

private val colorScheme = darkColorScheme(
    primary = Color(0xFFAF4448),
    onPrimary = Color(0xFFF1F1F1),
    secondary = Color(0xFFAF4448),
    onSecondary = Color(0xFFF1F1F1),
    tertiary = Color(0xFFAF4448),
    onTertiary = Color(0xFFF1F1F1),
    onError = Color(0xFFF1F1F1),
    error = Color(0xFFE48801),
    background = Color.Black,
    onBackground = Color(0xFFF1F1F1),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFCACACA),
    onPrimaryContainer = Color(0xFFF1F1F1),
    primaryContainer = Color(0xFFAF4448),
    surfaceContainerHighest = Color(0xFF1C1B1F),
)

@Composable
fun RootScreen(environment: AppEnvironment) {
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalAppEnvironment provides environment,
        LocalRootNavController provides navController
    ) {
        MaterialTheme(colorScheme = colorScheme) {
            Surface(color = MaterialTheme.colorScheme.background) {
                NavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    navController = navController,
                    startDestination = RootRoute.Dashboard
                ) {
                    composable<RootRoute.Dashboard> {
                        DashboardScreen()
                    }
                    composable<RootRoute.HabitDetails> {
                        val route: RootRoute.HabitDetails = it.toRoute()
                        HabitDetailsScreen(route.habitId)
                    }
                    composable<RootRoute.HabitEditing> {
                        val route: RootRoute.HabitEditing = it.toRoute()
                        HabitEditingScreen(route.habitId)
                    }
                    composable<RootRoute.HabitEventRecordsDetails> {
                        val route: RootRoute.HabitEventRecordsDetails = it.toRoute()
                        HabitEventRecordsDetailsScreen(route.habitId)
                    }

                    composable<RootRoute.HabitEventRecordEditing> {
                        val route: RootRoute.HabitEventRecordEditing = it.toRoute()
                        HabitEventRecordEditingScreen(
                            habitEventRecordId = route.habitEventRecordId,
                            habitId = route.habitId
                        )
                    }
                }
            }
        }
    }
}