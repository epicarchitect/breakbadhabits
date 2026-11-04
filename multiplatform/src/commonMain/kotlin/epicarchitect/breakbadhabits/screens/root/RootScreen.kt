package epicarchitect.breakbadhabits.screens.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import epicarchitect.breakbadhabits.infrastructure.DeepLinkChannel
import epicarchitect.breakbadhabits.screens.dashboard.DashboardScreen
import epicarchitect.breakbadhabits.screens.habits.details.HabitDetailsScreen
import epicarchitect.breakbadhabits.screens.habits.editing.HabitEditingScreen
import epicarchitect.breakbadhabits.screens.habits.eventRecords.details.HabitEventRecordsDetailsScreen
import epicarchitect.breakbadhabits.screens.habits.eventRecords.editing.HabitEventRecordEditingScreen
import epicarchitect.breakbadhabits.screens.habits.stages.HabitStagesScreen
import epicarchitect.breakbadhabits.screens.monetization.PaywallScreen
import epicarchitect.breakbadhabits.screens.settings.SettingsScreen
import epicarchitect.breakbadhabits.uikit.AppTheme
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic

val LocalNavBackStack = staticCompositionLocalOf<NavBackStack<NavKey>> {
    error("LocalNavBackStack not present")
}

fun NavBackStack<NavKey>.push(route: NavKey) {
    if (lastOrNull() != route) add(route)
}

@OptIn(ExperimentalSerializationApi::class)
private val rootSavedStateConfiguration = SavedStateConfiguration {
    serializersModule += SerializersModule {
        polymorphic(NavKey::class) {
            subclassesOfSealed<RootRoute>()
        }
    }
}

@Composable
fun RootScreen() {
    val navBackStack = rememberNavBackStack(rootSavedStateConfiguration, RootRoute.Dashboard)

    LaunchedEffect(Unit) {
        for (deepLink in DeepLinkChannel) {
            navBackStack.push(deepLink)
        }
    }

    CompositionLocalProvider(LocalNavBackStack provides navBackStack) {
        AppTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                NavDisplay(
                    modifier = Modifier.fillMaxSize(),
                    backStack = navBackStack,
                    onBack = navBackStack::removeLastOrNull,
                    entryProvider = entryProvider {
                        entry<RootRoute.Dashboard> {
                            DashboardScreen()
                        }
                        entry<RootRoute.Settings> {
                            SettingsScreen()
                        }
                        entry<RootRoute.HabitDetails> {
                            HabitDetailsScreen(it.habitId)
                        }
                        entry<RootRoute.Paywall> {
                            PaywallScreen(source = it.source)
                        }
                        entry<RootRoute.HabitEditing> {
                            HabitEditingScreen(it.habitId)
                        }
                        entry<RootRoute.HabitEventRecordsDetails> {
                            HabitEventRecordsDetailsScreen(it.habitId)
                        }
                        entry<RootRoute.HabitEventRecordEditing> {
                            HabitEventRecordEditingScreen(
                                habitEventRecordId = it.habitEventRecordId,
                                habitId = it.habitId
                            )
                        }
                        entry<RootRoute.HabitStages> {
                            HabitStagesScreen(it.habitId)
                        }
                    }
                )
            }
        }
    }
}
