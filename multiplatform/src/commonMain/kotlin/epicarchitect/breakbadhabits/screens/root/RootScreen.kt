package epicarchitect.breakbadhabits.screens.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
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

val LocalRootBackStack = staticCompositionLocalOf<NavBackStack<NavKey>> {
    error("LocalRootBackStack not present")
}

fun NavBackStack<NavKey>.push(route: RootRoute) {
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
private fun RootEntry(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        content()
    }
}

@Composable
fun RootScreen() {
    val backStack = rememberNavBackStack(
        rootSavedStateConfiguration,
        RootRoute.Dashboard
    )
    CompositionLocalProvider(
        LocalRootBackStack provides backStack
    ) {
        AppTheme {
            NavDisplay(
                modifier = Modifier.fillMaxSize(),
                backStack = backStack,
                onBack = backStack::removeLastOrNull,
                entryProvider = entryProvider {
                    entry<RootRoute.Dashboard> {
                        RootEntry {
                            DashboardScreen()
                        }
                    }
                    entry<RootRoute.Settings> {
                        RootEntry {
                            SettingsScreen()
                        }
                    }
                    entry<RootRoute.HabitDetails> {
                        RootEntry {
                            HabitDetailsScreen(it.habitId)
                        }
                    }
                    entry<RootRoute.Paywall> {
                        RootEntry {
                            PaywallScreen(source = it.source)
                        }
                    }
                    entry<RootRoute.HabitEditing> {
                        RootEntry {
                            HabitEditingScreen(it.habitId)
                        }
                    }
                    entry<RootRoute.HabitEventRecordsDetails> {
                        RootEntry {
                            HabitEventRecordsDetailsScreen(it.habitId)
                        }
                    }
                    entry<RootRoute.HabitEventRecordEditing> {
                        RootEntry {
                            HabitEventRecordEditingScreen(
                                habitEventRecordId = it.habitEventRecordId,
                                habitId = it.habitId
                            )
                        }
                    }
                    entry<RootRoute.HabitStages> {
                        RootEntry {
                            HabitStagesScreen(it.habitId)
                        }
                    }
                }
            )
        }
    }
}
